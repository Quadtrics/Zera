package com.example.zeraapp.activitys.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.NonNull
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.ChangePassword.ChangePasswordPojo
import com.example.zeraapp.utlis.*
import com.example.zeraapp.utlis.SharePreference.Companion.getStringPref
import com.example.zeraapp.utlis.SharePreference.Companion.userId
import com.ncorti.slidetoact.SlideToActView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class WithdrawActivity : AppCompatActivity() {

    lateinit var backWithdraw: ImageView
    lateinit var withdrawSwipe: SlideToActView
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
    lateinit var allDoneWithdraw: TextView
    lateinit var tv_amountWithdraw: TextView
    lateinit var t9_key_backspace: ImageView
    lateinit var et_withDraw: EditText
    lateinit var rlWith: RelativeLayout

    var apiService: ApiInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)
        findId()
    }

    private fun findId() {
        backWithdraw = findViewById(R.id.backWithdraw)
        withdrawSwipe = findViewById(R.id.withdrawSwipe)

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
        et_withDraw = findViewById(R.id.et_withDraw)
        allDoneWithdraw = findViewById(R.id.allDoneWithdraw)
        tv_amountWithdraw = findViewById(R.id.tv_amountWithdraw)
        rlWith = findViewById(R.id.rlWith)


        clicks()
        setData()
        setupEventCallbacks()
    }

    private fun setData() {
        tv_amountWithdraw.text = makeDollars(
            getStringPref(this@WithdrawActivity, SharePreference.userNetInterest).toString()
        )
    }

    private fun setupEventCallbacks() {
        withdrawSwipe.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(@NonNull view: SlideToActView) {
                setLog("Slider", "Status Completed")
                if (getEtText(et_withDraw).length <= 0) {
                    Common.showErrorFullMsg(
                        this@WithdrawActivity,
                        resources.getString(R.string.validation_money)
                    )
                    withdrawSwipe.resetSlider()
                } else {
                    val amtWithDraw: Double? = getEtText(et_withDraw).toDouble()
                    val amtAvailable: Double? = getStringPref(
                        this@WithdrawActivity,
                        SharePreference.userNetInterest
                    ).toString().toDouble()
                    if (amtWithDraw != null) {
                        if (amtWithDraw ==0.0) {
                            Common.showErrorFullMsg(
                                this@WithdrawActivity,
                                resources.getString(R.string.validation_zero_amount)
                            )
                            withdrawSwipe.resetSlider()
                        } else if (amtWithDraw > amtAvailable!!) {
                            Common.showErrorFullMsg(
                                this@WithdrawActivity,
                                resources.getString(R.string.validation_money_withdraw)
                            )
                            withdrawSwipe.resetSlider()
                        }else{
                            addPaymentAPI()
                        }
                    }
                }
                withdrawSwipe.resetSlider()
            }
        }
        withdrawSwipe.onSlideResetListener = object : SlideToActView.OnSlideResetListener {
            override fun onSlideReset(@NonNull view: SlideToActView) {
                setLog("Slider", "Status Reset")

            }
        }
        withdrawSwipe.onSlideUserFailedListener =
            object : SlideToActView.OnSlideUserFailedListener {
                override fun onSlideFailed(@NonNull view: SlideToActView, isOutside: Boolean) {
                    setLog("Slider", "Status Failed")

                }
            }
        withdrawSwipe.onSlideToActAnimationEventListener =
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

    private fun clicks() {

        backWithdraw.setOnClickListener {
            onBackPressed()
        }


        t9_key_1.setOnClickListener {
            et_withDraw.append("1")
        }
        t9_key_2.setOnClickListener {
            et_withDraw.append("2")
        }
        t9_key_3.setOnClickListener {
            et_withDraw.append("3")
        }
        t9_key_4.setOnClickListener {
            et_withDraw.append("4")
        }
        t9_key_5.setOnClickListener {
            et_withDraw.append("5")
        }
        t9_key_6.setOnClickListener {
            et_withDraw.append("6")
        }
        t9_key_7.setOnClickListener {
            et_withDraw.append("7")
        }
        t9_key_8.setOnClickListener {
            et_withDraw.append("8")
        }
        t9_key_9.setOnClickListener {
            et_withDraw.append("9")
        }
        t9_key_0.setOnClickListener {
            et_withDraw.append("0")
        }
        t9_key_00.setOnClickListener {
            et_withDraw.append("00")
        }
        allDoneWithdraw.setOnClickListener {
            rlWith.visibility = View.GONE
            var intent = Intent(this@WithdrawActivity, DashBoardActivity::class.java)
            intent.putExtra("nextWothdraw", "1")
            startActivity(intent)
            finish()
        }
        t9_key_backspace.setOnClickListener {
            val editable: Editable = et_withDraw.text
            val charCount = editable.length
            if (charCount > 0) {
                editable.delete(charCount - 1, charCount)
            }
        }
    }


    fun addPaymentAPI() {
        Log.e("WithDraw", "START")
        Log.v("WithDraw", "START" + getStringPref(this@WithdrawActivity, userId))
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        Common.showLoadingProgress(this@WithdrawActivity)
        apiService = ApiClient.getClientToken(
            getStringPref(
                this@WithdrawActivity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.addPayment(
            Integer.parseInt(getEtText(et_withDraw)),
            "withdrawal",
            getStringPref(this@WithdrawActivity, userId),
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
                        Log.e("WithDraw", "SUCCESS")
                       /* Toast.makeText(this@WithdrawActivity, response.message(), Toast.LENGTH_LONG)
                            .show()*/
                        rlWith.visibility = View.VISIBLE
                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("WithDraw", "401")

                        Toast.makeText(this@WithdrawActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@WithdrawActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("WithDraw", "400")
                        Toast.makeText(
                            this@WithdrawActivity,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        withdrawSwipe.resetSlider()

                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("WithDraw", response.code().toString())
                        Toast.makeText(this@WithdrawActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        withdrawSwipe.resetSlider()

                        return
                    }
                }
            }

            override fun onFailure(call: Call<ChangePasswordPojo?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("WithDraw", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
                withdrawSwipe.resetSlider()

            }
        })
    }
}