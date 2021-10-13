package com.example.zeraapp.activitys.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.NonNull
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.ChangePassword.ChangePasswordPojo
import com.example.zeraapp.utlis.*
import com.ncorti.slidetoact.SlideToActView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RedepositActivity : AppCompatActivity() {

    lateinit var tv_amountAvailable: TextView
    lateinit var backRedeposit:ImageView

    lateinit var redepositSwipe:SlideToActView
    private var timer: CountDownTimer? = null

    lateinit var t9_key_1: TextView
    lateinit var t9_key_2: TextView
    lateinit var t9_key_3: TextView
    lateinit var t9_key_4: TextView
    lateinit var t9_key_5: TextView
    lateinit var t9_key_6: TextView
    lateinit var t9_key_7: TextView
    lateinit var t9_key_8: TextView
    lateinit var t9_key_9: TextView
    lateinit var t9_key_0: TextView
    lateinit var t9_key_00: TextView
    lateinit var allDoneRedeposit: TextView
    lateinit var t9_key_backspace:ImageView
    lateinit var et_withDraw: EditText
    lateinit var rlRdSucess: RelativeLayout
    var apiService: ApiInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeposit)

        findID()
    }

    private fun findID() {
        tv_amountAvailable = findViewById(R.id.tv_amountAvailable)
        et_withDraw = findViewById(R.id.et_withDraw)
        
        
        backRedeposit = findViewById(R.id.backRedeposit)
        redepositSwipe = findViewById(R.id.redepositSwipe)


        t9_key_1 = findViewById(R.id.t9_key_1)
        t9_key_2 = findViewById(R.id.t9_key_2)
        t9_key_3 = findViewById(R.id.t9_key_3)
        t9_key_4 = findViewById(R.id.t9_key_4)
        t9_key_5 = findViewById(R.id.t9_key_5)
        t9_key_6 = findViewById(R.id.t9_key_6)
        t9_key_7 = findViewById(R.id.t9_key_7)
        t9_key_8 = findViewById(R.id.t9_key_8)
        t9_key_9 = findViewById(R.id.t9_key_9)
        t9_key_0 = findViewById(R.id.t9_key_0)
        t9_key_00 = findViewById(R.id.t9_key_00)
        t9_key_backspace = findViewById(R.id.t9_key_backspace)
        allDoneRedeposit = findViewById(R.id.allDoneRedeposit)
        rlRdSucess = findViewById(R.id.rlRdSucess)

        setData()

        clicks()
        setupEventCallbacks()
    }

    private fun setData() {
        tv_amountAvailable.text = makeDollars(
            SharePreference.getStringPref(this@RedepositActivity, SharePreference.userNetInterest).toString()
        )
    }

    private fun clicks() {
        backRedeposit.setOnClickListener {
            onBackPressed()
        }
/*
        redepositSwipe.setOnClickListener {
            timer = object : CountDownTimer(1000, 1500) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    rlRdSucess.visibility = View.VISIBLE
                }
            }
            timer!!.start()
        }
*/

        t9_key_1.setOnClickListener {
            et_withDraw.append("1");
        }
        t9_key_2.setOnClickListener {
            et_withDraw.append("2");
        }
        t9_key_3.setOnClickListener {
            et_withDraw.append("3");
        }
        t9_key_4.setOnClickListener {
            et_withDraw.append("4");
        }
        t9_key_5.setOnClickListener {
            et_withDraw.append("5");
        }
        t9_key_6.setOnClickListener {
            et_withDraw.append("6");
        }
        t9_key_7.setOnClickListener {
            et_withDraw.append("7");
        }
        t9_key_8.setOnClickListener {
            et_withDraw.append("8");
        }
        t9_key_9.setOnClickListener {
            et_withDraw.append("9");
        }
        t9_key_0.setOnClickListener {
            et_withDraw.append("0");
        }
        t9_key_00.setOnClickListener {
            et_withDraw.append("00");
        }
        t9_key_backspace.setOnClickListener {
            val editable: Editable = et_withDraw.getText()
            val charCount = editable.length
            if (charCount > 0) {
                editable.delete(charCount - 1, charCount)
            }
        }


        allDoneRedeposit.setOnClickListener {
            rlRdSucess.visibility = View.GONE
            var intent = Intent(this@RedepositActivity,DashBoardActivity::class.java)
            intent.putExtra("nextRedeposit","1")
            startActivity(intent)
            finish()
        }
    }
    private fun setupEventCallbacks() {
        redepositSwipe.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(@NonNull view: SlideToActView) {
                setLog("Slider", "Status Completed")
                if (getEtText(et_withDraw).length <= 0) {
                    Common.showErrorFullMsg(
                        this@RedepositActivity,
                        resources.getString(R.string.validation_money)
                    )
                    redepositSwipe.resetSlider()
                } else {
                    val amtWithDraw: Double? = getEtText(et_withDraw).toDouble()
                    val amtAvailable: Double? = SharePreference.getStringPref(
                        this@RedepositActivity,
                        SharePreference.userNetInterest
                    ).toString().toDouble()
                    if (amtWithDraw != null) {
                        if (amtWithDraw ==0.0) {
                            Common.showErrorFullMsg(
                                this@RedepositActivity,
                                resources.getString(R.string.validation_zeroDeposit_amount)                    )
                            redepositSwipe.resetSlider()
                        } else if (amtWithDraw > amtAvailable!!) {
                            Common.showErrorFullMsg(
                                this@RedepositActivity,
                                resources.getString(R.string.validation_money_redeposit)
                            )
                            redepositSwipe.resetSlider()
                        }else{
                            addPaymentAPI()
                        }
                    }
                }
                redepositSwipe.resetSlider()
            }
        }
        redepositSwipe.onSlideResetListener = object : SlideToActView.OnSlideResetListener {
            override fun onSlideReset(@NonNull view: SlideToActView) {
                setLog("Slider", "Status Reset")

            }
        }
        redepositSwipe.onSlideUserFailedListener =
            object : SlideToActView.OnSlideUserFailedListener {
                override fun onSlideFailed(@NonNull view: SlideToActView, isOutside: Boolean) {
                    setLog("Slider", "Status Failed")

                }
            }
        redepositSwipe.onSlideToActAnimationEventListener =
            object : SlideToActView.OnSlideToActAnimationEventListener {
                override fun onSlideCompleteAnimationStarted(
                    @NonNull view: SlideToActView,
                    threshold: Float
                ) {
                    setLog("Slider", "Status Animate " + threshold)

                }

                override fun onSlideCompleteAnimationEnded(@NonNull view: SlideToActView) {
                    setLog("Slider", "Status Anim Ended")
                }

                override fun onSlideResetAnimationStarted(@NonNull view: SlideToActView) {
                    setLog("Slider", "Status Anim Start")
                }

                override fun onSlideResetAnimationEnded(@NonNull view: SlideToActView) {
                    setLog("Slider", "Status Reset Anim Ended")
                }
            }
    }
    fun addPaymentAPI() {
        Log.e("Redeposit", "START")
        Log.v("Redeposit", "START" + SharePreference.getStringPref(
            this@RedepositActivity,
            SharePreference.userId
        )
        )
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        Common.showLoadingProgress(this@RedepositActivity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@RedepositActivity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.addPayment(
            Integer.parseInt(getEtText(et_withDraw)),
            "redeposit",
            SharePreference.getStringPref(this@RedepositActivity, SharePreference.userId),
            currentDate.toString(),

            ).enqueue(object :
            Callback<ChangePasswordPojo?> {
            override fun onResponse(
                call: Call<ChangePasswordPojo?>,
                response: Response<ChangePasswordPojo?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
/*
                        Toast.makeText(this@RedepositActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
*/
                        rlRdSucess.visibility = View.VISIBLE
                        Log.e("Redeposit", "SUCCESS")
                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("Redeposit", "401")

                        Toast.makeText(this@RedepositActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@RedepositActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("Redeposit", "400")
                        Toast.makeText(
                            this@RedepositActivity,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        redepositSwipe.resetSlider()

                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("Redeposit", response.code().toString())
                        Toast.makeText(this@RedepositActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        redepositSwipe.resetSlider()

                        return
                    }
                }
            }

            override fun onFailure(call: Call<ChangePasswordPojo?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("Redeposit", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
                redepositSwipe.resetSlider()

            }
        })
    }


}