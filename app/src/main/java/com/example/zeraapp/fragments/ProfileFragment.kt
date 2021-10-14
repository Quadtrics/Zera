package com.example.zeraapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.example.zeraapp.R
import com.example.zeraapp.activitys.activity.AuthorizedUserActivity
import com.example.zeraapp.activitys.activity.GeneralInfoActivity
import com.example.zeraapp.activitys.activity.MainActivity
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.ChangePassword.ChangePasswordPojo
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import com.example.zeraapp.utlis.Common.BaseImageUrl
import com.example.zeraapp.utlis.SharePreference.Companion.getStringPref
import com.example.zeraapp.utlis.SharePreference.Companion.userFirstName
import com.example.zeraapp.utlis.SharePreference.Companion.userName
import com.github.drjacky.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


class
ProfileFragment : Fragment() {

    lateinit var generalInfo: LinearLayout
    lateinit var tv_userName: TextView
    lateinit var tv_clientId: TextView
    lateinit var llAddUser: LinearLayout
    lateinit var llChangePass: LinearLayout
    lateinit var signOut: LinearLayout
    lateinit var civ_profile: ImageView
    lateinit var rl_profileCamera: RelativeLayout
    lateinit var rl_Language: RelativeLayout
    lateinit var civ_mexico: CircleImageView
    lateinit var civ_america: CircleImageView
    private lateinit var ctx: Context
    var encodedImage: String = ""
    var lastLang: String =""
    var fileImage: File? = null
    var apiService: ApiInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        findID(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun findID(view: View) {

        generalInfo = view.findViewById(R.id.generalInfo)
        llAddUser = view.findViewById(R.id.llAddUser)
        llChangePass = view.findViewById(R.id.llChangePass)
        signOut = view.findViewById(R.id.signOut)

        rl_profileCamera = view.findViewById(R.id.rl_profileCamera)
        rl_Language = view.findViewById(R.id.rl_Language)
        civ_mexico = view.findViewById(R.id.civ_mexico)
        civ_america = view.findViewById(R.id.civ_america)
        civ_profile = view.findViewById(R.id.civ_profile)
        tv_userName = view.findViewById(R.id.tv_userName)
        tv_clientId = view.findViewById(R.id.tv_clientId)
        setData()
        clicks()
        setAppLanguage()
//        setAppLanguage()

    }

    private fun setData() {
        tv_clientId.text = showHashIds(
            SharePreference.getStringPref(
                ctx,
                SharePreference.clientId
            ).toString()
        )
        setLog("UserCheck", " " + getStringPref(ctx, userName).toString())
        setLog("UserCheck", " " + getStringPref(ctx, userFirstName).toString())
        var varName = getStringPref(ctx, userName).toString()
        if (varName.isEmpty() || varName.equals(""))
            tv_userName.text = extractWord(getStringPref(ctx, userFirstName).toString())
        else
            tv_userName.text = extractWord(getStringPref(ctx, userName).toString())

        val imagePath: String? = SharePreference.getStringPref(
            ctx,
            SharePreference.userProfileImage
        )
        if (imagePath != null) {
            if (imagePath.isNotEmpty()) {
                Picasso.get()
                    .load(
                        imagePath
                    ).placeholder(R.drawable.app_icon)
                    .into(civ_profile)
            }
        }

    }

    private fun clicks() {
        generalInfo.setOnClickListener {
            val intent = Intent(context, GeneralInfoActivity::class.java)
            startActivity(intent)
        }
        llAddUser.setOnClickListener {
            val intent = Intent(context, AuthorizedUserActivity::class.java)
            startActivity(intent)
        }

        rl_profileCamera.setOnClickListener {
            ImagePicker.with(context as Activity)
                .crop()
                .createIntentFromDialog { launcher.launch(it) }

        }
        rl_Language.setOnClickListener {
            if(lastLang =="en") {
                SharePreference.setStringPref(ctx as Activity, SharePreference.appLanguage, "es")
                lastLang = "es"
            }
            else {
                SharePreference.setStringPref(ctx as Activity, SharePreference.appLanguage, "en")
                lastLang = "en"
            }
            setLocale(ctx as Activity, lastLang,true)
        }
        llChangePass.setOnClickListener {
            /*    val intent = Intent(context, ChangePasswordActivity::class.java)
                startActivity(intent)
            */showDialog()
//            rl_changePassword.visibility = VISIBLE

        }
        signOut.setOnClickListener {
            exitDialogBox()
        }
    }

    private fun setAppLanguage() {
        lastLang = getStringPref(ctx, SharePreference.appLanguage).toString()
        setLog("MyLanguage",lastLang+" ")
        if(lastLang.equals("es")){
            civ_america.setColorFilter(getColor(ctx, R.color.text_gray), PorterDuff.Mode.DARKEN)
            civ_mexico.colorFilter = null
            civ_america.circleBackgroundColor = getColor(ctx, R.color.colorPrimary)
            civ_mexico.circleBackgroundColor = getColor(ctx, R.color.text_gray)
        } else {
            civ_mexico.setColorFilter(getColor(ctx, R.color.text_gray), PorterDuff.Mode.DARKEN)
            civ_america.colorFilter = null
            civ_mexico.circleBackgroundColor = getColor(ctx, R.color.colorPrimary)
            civ_america.circleBackgroundColor = getColor(ctx, R.color.text_gray)
        }
    }


    private fun exitDialogBox() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout_layout, null)
        val customDialog = AlertDialog.Builder(ctx)
            .setView(dialogView)
            .show()
        val rl_ok = dialogView.findViewById<RelativeLayout>(R.id.rl_ok)
        rl_ok.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            customDialog.dismiss()
        }
        val rl_cancel = dialogView.findViewById<RelativeLayout>(R.id.rl_cancel)
        rl_cancel.setOnClickListener {
            customDialog.dismiss()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(ctx as Activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.change_password_dialog)
        var et_currentPassword = dialog.findViewById(R.id.et_currentPassword) as EditText
        var et_newPassword = dialog.findViewById(R.id.et_newPassword) as EditText
        var et_confirmPassword = dialog.findViewById(R.id.et_confirmPassword) as EditText
        var tv_savePassword = dialog.findViewById(R.id.tv_savePassword) as TextView
        var iv_close = dialog.findViewById(R.id.iv_close) as ImageView
        iv_close.setOnClickListener {
            dialog.dismiss()
        }
        tv_savePassword.setOnClickListener {
            if (getEtText(et_currentPassword).equals("")) {
                Common.showErrorFullMsg(
                    ctx as Activity,
                    resources.getString(R.string.validation_oldpassword)
                )
            } else if (getEtText(et_newPassword).equals("")) {
                Common.showErrorFullMsg(
                    ctx as Activity,
                    resources.getString(R.string.validation_newpassword)
                )
            } else if (getEtText(et_newPassword).length < 6) {
                Common.showErrorFullMsg(
                    ctx as Activity,
                    resources.getString(R.string.validation_valid_password)
                )
            } else if (getEtText(et_confirmPassword).equals("")) {
                Common.showErrorFullMsg(
                    ctx as Activity,
                    resources.getString(R.string.validation_cpassword)
                )
            } else if (!getEtText(et_confirmPassword).equals(getEtText(et_newPassword))) {
                Common.showErrorFullMsg(
                    ctx as Activity,
                    resources.getString(R.string.validation_valid_cpassword)
                )
            } else {
                changePassAPI(
                    getEtText(et_currentPassword),
                    getEtText(et_newPassword),
                    getEtText(et_confirmPassword),
                    dialog
                )
            }
        }
        dialog.show()

    }


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                // Use the uri to load the image
                civ_profile.setImageURI(uri)
                val resolver = requireActivity().contentResolver

                val imageStream: InputStream = resolver.openInputStream(uri)!!
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                encodedImage = encodeImage(selectedImage).toString()
                setLog("MYIMageString", encodedImage + "  ")

                fileImage = File(uri.path)
                val requestFile: RequestBody = fileImage!!
                    .asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val body: MultipartBody.Part =
                    createFormData("image", fileImage!!.name, requestFile)
                if (!encodedImage.isEmpty() && Common.isCheckNetwork(ctx)) {
                    if (Common.isCheckNetwork(ctx)) {
                        updateUserInfo(body)
                    } else {
                        Common.alertErrorOrValidationDialog(
                            context as Activity,
                            resources.getString(R.string.no_internet)
                        )
                    }
                }
            }
        }

    private fun updateUserInfo(body: MultipartBody.Part) {

        Log.e("LOGIN", "START")
        Common.showLoadingProgress(context as Activity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                ctx,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.userUpdate(
            SharePreference.getStringPref(ctx, SharePreference.userId).toString(),
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
                        setLog("MyNewImage", BaseImageUrl + loginList?.data!!.image.toString())
                        SharePreference.setStringPref(
                            ctx,
                            SharePreference.userProfileImage,
                            BaseImageUrl + loginList.data!!.image.toString()
                        )
                        setData()


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
                            ctx,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()

                        val intent = Intent(ctx, MainActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(
                            ctx,
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
                            ctx,
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


    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return encodeToString(b, DEFAULT)
    }

    fun changePassAPI(currentPass: String, newPass: String, confirmPass: String, dialog: Dialog) {
        Log.e("changePassword", "START")
        Common.showLoadingProgress(ctx as Activity)
        apiService = ApiClient.getClientToken(
            getStringPref(
                ctx as Activity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.changePassword(
            currentPass,
            newPass,
            confirmPass
        )!!.enqueue(object : Callback<ChangePasswordPojo?> {
            override fun onResponse(
                call: Call<ChangePasswordPojo?>,
                response: Response<ChangePasswordPojo?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: ChangePasswordPojo? = response.body()
                        Log.e("changePassword", "SUCCESS")
                        showToast(ctx as Activity, getString(R.string.passwordSuccess))
                        dialog.dismiss()
                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("changePassword", "401")

                        Toast.makeText(ctx as Activity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(ctx as Activity, MainActivity::class.java)
                        startActivity(intent)


                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("changePassword", "400")
                        Toast.makeText(ctx as Activity, "Invalid Credentials", Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("changePassword", response.code().toString())
                        Toast.makeText(ctx as Activity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<ChangePasswordPojo?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("changePassword", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }
        })
    }


}