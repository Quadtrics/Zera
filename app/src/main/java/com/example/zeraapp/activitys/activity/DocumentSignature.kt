package com.example.zeraapp.activitys.activity

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Dialog
import android.content.ContextWrapper
import android.content.Intent
import android.gesture.GestureOverlayView
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.annotation.Nullable
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class DocumentSignature : AppCompatActivity() {
    lateinit var rl_addSignatue: RelativeLayout
    lateinit var ll_pageAlpha: LinearLayout
    var path: String? = null
    var bitmap: Bitmap? = null
    var gestureTouch = false
    var hasSignature = false
    lateinit var tv_saveDocument: TextView
    lateinit var tv_interestRate: TextView
    lateinit var tv_startingAccount: TextView
    lateinit var tv_termLength: TextView
    lateinit var tv_maturityDate: TextView
    lateinit var tv_date: TextView
    lateinit var iv_signatue: ImageView
    var apiService: ApiInterface? = null
    var depositAmount: String? = null
    val PERMISSION_REQUEST_CODE = 2296

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_signature)
        setIds()
        setListener()
        setData()
    }

    private fun setData() {
        tv_date.text = resources.getString(R.string.date) + " " + addMonths(0, "MM/dd/yyyy")
        tv_termLength.text = resources.getString(R.string.termLength) + " " + intent.getStringExtra(
            "timePeriodSelected"
        )
        tv_maturityDate.text = resources.getString(R.string.maturityDate) + " " + convertDate(
            intent.getStringExtra(
                "dateOfMaturity"
            ).toString()
        ,"yyyy-MM-dd","MM/dd/yyyy")
/*
        tv_maturityDate.text = resources.getString(R.string.maturityDate) + " " + intent.getStringExtra(
            "dateOfMaturity"
        )
*/
        tv_interestRate.text = resources.getString(R.string.interestRate) + " 5%/Month"
        depositAmount = intent.getStringExtra("depositAmount").toString()
        tv_startingAccount.text = resources.getString(R.string.StartingAmount )+ makeDollars(
            intent.getStringExtra("depositAmount").toString()
        )
    }

    private fun setIds() {
        ll_pageAlpha = findViewById(R.id.ll_pageAlpha)
        rl_addSignatue = findViewById(R.id.rl_addSignatue)
        tv_saveDocument = findViewById(R.id.tv_saveDocument)
        tv_startingAccount = findViewById(R.id.tv_startingAccount)
        tv_interestRate = findViewById(R.id.tv_interestRate)
        tv_maturityDate = findViewById(R.id.tv_maturityDate)
        tv_termLength = findViewById(R.id.tv_termLength)
        tv_date = findViewById(R.id.tv_date)
        iv_signatue = findViewById(R.id.iv_signatue)
    }

    private fun setListener() {
        rl_addSignatue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

//                managePermissionsWrite()
                showSignatureBox()
            }
        })
        tv_saveDocument.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // TODO Auto-generated method stub
                if (hasSignature) {
                    var bitmapPdf =
                        LoadBitmap(
                            ll_pageAlpha,
                            ll_pageAlpha.width,
                            ll_pageAlpha.height
                        )
                    createPdf(bitmapPdf)
                } else
                    showToast(this@DocumentSignature, "Please sign the document first")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun managePermissionsWrite() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                this@DocumentSignature,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun showSignatureBox() {
        val dialog = Dialog(this@DocumentSignature)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.signature_pad_layout)
        var ges_signaturePad = dialog.findViewById(R.id.ges_signaturePad) as GestureOverlayView
        var tv_saveSignature = dialog.findViewById(R.id.tv_saveSignature) as TextView
        ges_signaturePad.isDrawingCacheEnabled = true
        ges_signaturePad.isAlwaysDrawnWithCacheEnabled = true
        ges_signaturePad.isHapticFeedbackEnabled = false
        ges_signaturePad.cancelLongPress()
        ges_signaturePad.cancelClearAnimation()
        var hasSignature = false
        ges_signaturePad.addOnGestureListener(object : GestureOverlayView.OnGestureListener {
            override fun onGesture(arg0: GestureOverlayView?, arg1: MotionEvent?) {
//                 TODO Auto-generated method stub
            }

            override fun onGestureCancelled(
                arg0: GestureOverlayView?,
                arg1: MotionEvent?
            ) {
                setLog("GestureCheck", "ScollCancelled")
            }

            override fun onGestureEnded(arg0: GestureOverlayView?, arg1: MotionEvent?) {
                // TODO Auto-generated method stub
                setLog("GestureCheck", "ScollEnd")
                hasSignature = true
            }

            override fun onGestureStarted(
                arg0: GestureOverlayView?,
                arg1: MotionEvent
            ) {
                // TODO Auto-generated method stub
                gestureTouch = arg1.action != MotionEvent.ACTION_MOVE
            }
        })
        var iv_close = dialog.findViewById(R.id.iv_close) as ImageView
        iv_close.setOnClickListener {
            dialog.dismiss()
        }
        tv_saveSignature.setOnClickListener {
            if (hasSignature) {
                bitmap = Bitmap.createBitmap(ges_signaturePad.drawingCache)
                setSignature()
                dialog.dismiss()
            } else {
                showToast(this@DocumentSignature, "No signature detected")
            }
        }
        dialog.show()
    }

    private fun setSignature() {
        iv_signatue.setImageBitmap(bitmap)
        hasSignature = true
    }

    private fun LoadBitmap(v: View, width: Int, height: Int): Bitmap? {
        val bitmaps = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        val canvas = Canvas(bitmaps)
        v.draw(canvas)
        return bitmaps
    }

    private fun createPdf(bitmapPdf: Bitmap?) {
        var isDocument = false
        val convertHighet = ll_pageAlpha.height
        val convertWidth = ll_pageAlpha.width
        val document = PdfDocument()
        val pageInfo = PageInfo.Builder(convertWidth, convertHighet, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        canvas.drawPaint(paint)
        val newBits = Bitmap.createScaledBitmap(bitmapPdf!!, convertWidth, convertHighet, true)
        paint.color = Color.BLUE
        canvas.drawBitmap(newBits, 0f, 0f, null)
        document.finishPage(page)
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val filePath = File(directory, "ZeraContract" + ".pdf")

        try {
            val fileOutputStream = FileOutputStream(filePath)
            document.writeTo(fileOutputStream)
            isDocument = true
        } catch (e: IOException) {
            isDocument = false
            e.printStackTrace()
            Toast.makeText(this, "Something wrong: $e", Toast.LENGTH_LONG).show()
        } ////////////////////
        setLog("MydocPage", filePath.absolutePath)
        // close the document
        document.close()
        if (isDocument) {
            Toast.makeText(this, "Pdf Created Successfully", Toast.LENGTH_SHORT).show()

//            val value = HashMap<String, RequestBody>()
//            var name1: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)

            setLog("MydocPage", filePath.name)

            val requestFile: RequestBody = filePath
                .asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("contract", filePath.name, requestFile)
            if (Common.isCheckNetwork(this@DocumentSignature)) {
                if (Common.isCheckNetwork(this@DocumentSignature)) {
                    uploadContractFile(body)
//                openGeneratedPDF(filePath.absolutePath)
                } else {
                    Common.alertErrorOrValidationDialog(
                        this@DocumentSignature,
                        resources.getString(R.string.no_internet)
                    )
                }
            }
        }
//        generatePDF()
    }

    private fun uploadContractFile(body: MultipartBody.Part) {
        Log.e("LOGIN", "START")

        val value = HashMap<String, RequestBody>()
        var name1: RequestBody =
            SharePreference.getStringPref(this@DocumentSignature, SharePreference.userId)
                .toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())

        value.put("user_ID", name1)
        Common.showLoadingProgress(this@DocumentSignature)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@DocumentSignature,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.uploadContract(
            value,
            body
        )!!.enqueue(object :
            Callback<SignupPojo?> {
            override fun onResponse(
                call: Call<SignupPojo?>,
                response: Response<SignupPojo?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: SignupPojo? = response.body()
                        setLog(
                            "MyNewImage",
                            "Success"
                        )
                        var intent =
                            Intent(this@DocumentSignature, AddFundActivity::class.java)
                        intent.putExtra("Amount", depositAmount)
                        intent.putExtra("path", "signUp")
                        startActivity(intent)

                        /*
                               SharePreference.setStringPref(
                                   ctx,
                                   SharePreference.user_name,
                                   getEtText(et_userName)
                               )

                               onBackPressed()
                        */       Log.e("LOGIN", "SUCCESSImageUpload")

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "401")

                        Toast.makeText(
                            this@DocumentSignature,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()

                        val intent = Intent(this@DocumentSignature, MainActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(
                            this@DocumentSignature,
                            "Invalid Credentials",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", response.code().toString())
                        Toast.makeText(
                            this@DocumentSignature,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<SignupPojo?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("LOGINN", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }


        })
    }


}