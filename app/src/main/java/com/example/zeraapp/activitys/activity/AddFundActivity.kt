package com.example.zeraapp.activitys.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.NonNull
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.ChangePassword.ChangePasswordPojo
import com.example.zeraapp.utlis.Common
import com.example.zeraapp.utlis.SharePreference
import com.example.zeraapp.utlis.getEtText
import com.example.zeraapp.utlis.setLog
import com.ncorti.slidetoact.SlideToActView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AddFundActivity : AppCompatActivity() {
    lateinit var backAddFund:ImageView
    lateinit var t9_key_1:TextView
    lateinit var t9_key_2:TextView
    lateinit var t9_key_3:TextView
    lateinit var t9_key_4:TextView
    lateinit var t9_key_5:TextView
    lateinit var t9_key_6:TextView
    lateinit var t9_key_7:TextView
    lateinit var t9_key_8:TextView
    lateinit var t9_key_9:TextView
    lateinit var t9_key_0:TextView
    lateinit var t9_key_00:TextView
    lateinit var t9_key_backspace:ImageView
    lateinit var et_depositAmount:EditText
    lateinit var depositSwipe:SlideToActView
    var apiService: ApiInterface? = null

    private var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fund)
         findId()
        setData()
    }

    private fun setData() {
        if(intent.hasExtra("path")){
            et_depositAmount.setText(intent.getStringExtra("Amount"))
            makeDisablesOn()
        }
    }

    private fun makeDisablesOn() {
        t9_key_1.setClickable(false)
        t9_key_2.setClickable(false)
        t9_key_3.setClickable(false)
        t9_key_4.setClickable(false)
        t9_key_5.setClickable(false)
        t9_key_6.setClickable(false)
        t9_key_7.setClickable(false)
        t9_key_8.setClickable(false)
        t9_key_9.setClickable(false)
        t9_key_0.setClickable(false)
        t9_key_00.setClickable(false)
        t9_key_backspace.setClickable(false)
    }

    private fun findId() {
        backAddFund = findViewById(R.id.backAddFund)
        depositSwipe = findViewById(R.id.depositSwipe)

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
        et_depositAmount = findViewById(R.id.et_depositAmount)

        clicks()
    }

    private fun clicks() {
        depositSwipe.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(@NonNull view: SlideToActView) {
                setLog("Slider", "Status Completed")
                if (getEtText(et_depositAmount).length <= 0) {
                    Common.showErrorFullMsg(
                        this@AddFundActivity,
                        resources.getString(R.string.validation_money)
                    )
                    depositSwipe.resetSlider()
                } else {
                    val amtDeposit: Double? = getEtText(et_depositAmount).toDouble()
                    if (amtDeposit != null) {
                        if (amtDeposit ==0.0) {
                            Common.showErrorFullMsg(
                                this@AddFundActivity,
                                resources.getString(R.string.validation_zeroDeposit_amount)
                            )
                            depositSwipe.resetSlider()
                        } else{
                            addPaymentAPI()
                        }
                    }
                }
                depositSwipe.resetSlider()
            }
        }
        depositSwipe.onSlideResetListener = object : SlideToActView.OnSlideResetListener {
            override fun onSlideReset(@NonNull view: SlideToActView) {
                setLog("Slider", "Status Reset")

            }
        }
        depositSwipe.onSlideUserFailedListener =
            object : SlideToActView.OnSlideUserFailedListener {
                override fun onSlideFailed(@NonNull view: SlideToActView, isOutside: Boolean) {
                    setLog("Slider", "Status Failed")

                }
            }
        depositSwipe.onSlideToActAnimationEventListener =
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

      
        backAddFund.setOnClickListener {
            onBackPressed()
        }

        t9_key_1.setOnClickListener {
            et_depositAmount.append("1");
        }
        t9_key_2.setOnClickListener {
            et_depositAmount.append("2");
        }
        t9_key_3.setOnClickListener {
            et_depositAmount.append("3");
        }
        t9_key_4.setOnClickListener {
            et_depositAmount.append("4");
        }
        t9_key_5.setOnClickListener {
            et_depositAmount.append("5");
        }
        t9_key_6.setOnClickListener {
            et_depositAmount.append("6");
        }
        t9_key_7.setOnClickListener {
            et_depositAmount.append("7");
        }
        t9_key_8.setOnClickListener {
            et_depositAmount.append("8");
        }
        t9_key_9.setOnClickListener {
            et_depositAmount.append("9");
        }
        t9_key_0.setOnClickListener {
            et_depositAmount.append("0");
        }
        t9_key_00.setOnClickListener {
            et_depositAmount.append("00");
        }
        t9_key_backspace.setOnClickListener {
            val editable: Editable = et_depositAmount.getText()
            val charCount = editable.length
            if (charCount > 0) {
                editable.delete(charCount - 1, charCount)
            }
        }
    }
    fun addPaymentAPI() {
        Log.e("Deposit", "START")
        Log.v("Deposit", "START" + SharePreference.getStringPref(
            this@AddFundActivity,
            SharePreference.userId
        )
        )
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        Common.showLoadingProgress(this@AddFundActivity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@AddFundActivity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.addPayment(
            Integer.parseInt(getEtText(et_depositAmount)),
            "deposit",
            SharePreference.getStringPref(this@AddFundActivity, SharePreference.userId),
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
                        Log.e("Deposit", "SUCCESS")
          /*              Toast.makeText(this@AddFundActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
          */              var intent = Intent(this@AddFundActivity, DashBoardActivity::class.java)
                        intent.putExtra("nextWothdraw", "1")
                        startActivity(intent)
                        finish()
                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("Deposit", "401")

                        Toast.makeText(this@AddFundActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        var intent = Intent(this@AddFundActivity,DashBoardActivity::class.java)
                        startActivity(intent)
                        finish()


                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("Deposit", "400")
                        Toast.makeText(
                            this@AddFundActivity,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        depositSwipe.resetSlider()

                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("Deposit", response.code().toString())
                        Toast.makeText(this@AddFundActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        depositSwipe.resetSlider()

                        return
                    }
                }
            }

            override fun onFailure(call: Call<ChangePasswordPojo?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("Deposit", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
                depositSwipe.resetSlider()

            }
        })
    }

}