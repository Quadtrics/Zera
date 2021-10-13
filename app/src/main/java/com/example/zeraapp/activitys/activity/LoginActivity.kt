package com.example.zeraapp.activitys.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.ForgotPassword.ForgotPasswordPojo
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import com.example.zeraapp.utlis.Common.BaseImageUrl
import com.example.zeraapp.utlis.Common.showErrorFullMsg
import com.example.zeraapp.utlis.SharePreference.Companion.clientId
import com.example.zeraapp.utlis.SharePreference.Companion.incomingBalance
import com.example.zeraapp.utlis.SharePreference.Companion.loginToken
import com.example.zeraapp.utlis.SharePreference.Companion.principalBalance
import com.example.zeraapp.utlis.SharePreference.Companion.setStringPref
import com.example.zeraapp.utlis.SharePreference.Companion.userEmail
import com.example.zeraapp.utlis.SharePreference.Companion.userFirstName
import com.example.zeraapp.utlis.SharePreference.Companion.userId
import com.example.zeraapp.utlis.SharePreference.Companion.userLastName
import com.example.zeraapp.utlis.SharePreference.Companion.userMobile
import com.example.zeraapp.utlis.SharePreference.Companion.userName
import com.example.zeraapp.utlis.SharePreference.Companion.userNetBalance
import com.example.zeraapp.utlis.SharePreference.Companion.userNetEarn
import com.example.zeraapp.utlis.SharePreference.Companion.userNetInterest
import com.example.zeraapp.utlis.SharePreference.Companion.userProfileImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var signUp: TextView
    lateinit var forgotPass: TextView
    lateinit var forgotPasswordBtn: TextView
    lateinit var closeForgot: ImageView
    lateinit var loginBtn: RelativeLayout
    lateinit var rlForgot: RelativeLayout
    lateinit var loginEmail: EditText
    lateinit var loginPass: EditText
    lateinit var forgotEmail: EditText
    var apiService: ApiInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findID()
    }

    private fun findID() {
        signUp = findViewById(R.id.tvLSign)
        loginBtn = findViewById(R.id.loginBtn)
        forgotPass = findViewById(R.id.forgotPass)
        closeForgot = findViewById(R.id.closeForgot)
        rlForgot = findViewById(R.id.rlForgot)
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn)

        loginEmail = findViewById(R.id.loginEmail)
        loginPass = findViewById(R.id.loginPass)
        forgotEmail = findViewById(R.id.forgotEmail)

        click()
    }

    private fun click() {
        signUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        forgotPass.setOnClickListener {
            rlForgot.visibility = View.VISIBLE
        }
        forgotPasswordBtn.setOnClickListener {
            showErrorFullMsg(this@LoginActivity, getEtText(forgotEmail))
            if (forgotEmail.text.toString().equals("")) {
                showErrorFullMsg(this@LoginActivity, resources.getString(R.string.validation_email))
            } else if (!Common.isValidEmail(forgotEmail.text.toString())) {
                showErrorFullMsg(
                    this@LoginActivity,
                    resources.getString(R.string.validation_email)
                )
            } else {
                forgotPassAPI()
            }

        }
        loginBtn.setOnClickListener {

            if (loginEmail.text.toString().equals("")) {
                Common.showErrorFullMsg(
                    this@LoginActivity,
                    resources.getString(R.string.validation_email)
                )
            } else if (!Common.isValidEmail(loginEmail.text.toString())) {
                Common.showErrorFullMsg(
                    this@LoginActivity,
                    resources.getString(R.string.validation_email)
                )
            } else if (loginPass.text.toString().equals("")) {
                Common.showErrorFullMsg(
                    this@LoginActivity,
                    resources.getString(R.string.validation_password)
                )
            } else {

                if (Common.isCheckNetwork(this@LoginActivity)) {
                    loginAPI()
                } else {
                    Common.alertErrorOrValidationDialog(
                        this@LoginActivity,
                        resources.getString(R.string.no_internet)
                    )
                }


            }

        }
        closeForgot.setOnClickListener {
            rlForgot.visibility = View.GONE
        }

    }


    fun loginAPI() {
        Log.e("LOGIN", "START")
        Common.showLoadingProgress(this@LoginActivity)
        apiService = ApiClient.getClient()!!.create(ApiInterface::class.java)
        apiService!!.Login(
            loginEmail.text.toString(),
            loginPass.text.toString(),
            "android",
            "afdsasdfsadfsda"
        )!!.enqueue(object : Callback<SignupPojo?> {
            override fun onResponse(call: Call<SignupPojo?>, response: Response<SignupPojo?>) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: SignupPojo? = response.body()
                        Log.e("LOGIN", "SUCCESS")
                        setLog("MyDeviceToken", loginList?.data!!.token.toString())
                        setStringPref(this@LoginActivity, userId, loginList.data!!.id)
                        setStringPref(this@LoginActivity, loginToken, loginList.data!!.token)
                        setStringPref(
                            this@LoginActivity,
                            userFirstName,
                            loginList.data!!.first_name
                        )
                        setStringPref(this@LoginActivity, userLastName, loginList.data!!.last_name)
                        setStringPref(this@LoginActivity, userEmail, loginList.data!!.email)
                        setStringPref(
                            this@LoginActivity,
                            userNetBalance,
                            loginList.data!!.net_balance.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            userNetEarn,
                            loginList.data!!.net_earned.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            userNetInterest,
                            loginList.data!!.net_interest.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            incomingBalance,
                            loginList.data!!.incoming.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            principalBalance,
                            loginList.data!!.principle_balance.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            userMobile,
                            loginList.data!!.phoneNumber.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            clientId,
                            loginList.data!!.client_id.toString()
                        )
                        var checkUserName: String? = loginList.data!!.user_name.toString()
                        if (checkUserName?.isNotEmpty()!!) {
                            setStringPref(
                                this@LoginActivity,
                                userName, extractWord(loginList.data!!.user_name.toString())
                            )
                        } else {
                            setStringPref(
                                this@LoginActivity,
                                userName, extractWord(loginList.data!!.first_name.toString())
                            )
                        }


                        setStringPref(
                            this@LoginActivity,
                            userProfileImage,
                            BaseImageUrl + loginList.data!!.image.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            SharePreference.maturity_date,
                            loginList.data!!.maturity_date.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            SharePreference.isFirstDeposit,
                            loginList.data!!.isFirstDeposit.toString()
                        )
                        setStringPref(
                            this@LoginActivity,
                            SharePreference.contract,
                            loginList.data!!.contract.toString()
                        )

                        val dateMaturity = loginList.data!!.maturity_date.toString()
                        val isFirstDepositAmount = loginList.data!!.isFirstDeposit.toString()
                        val contract: String = loginList.data!!.contract.toString()
                        if (contract.equals("null")) {
                            val intent =
                                Intent(this@LoginActivity, DepositAmmountActivity::class.java)
                            startActivity(intent)
                        } else if (isFirstDepositAmount.equals("0")) {
                            val intent =
                                Intent(this@LoginActivity, DepositAmmountActivity::class.java)
                            startActivity(intent)
                        } else if (dateMaturity.equals("0") || dateMaturity.isEmpty()) {
                            val intent =
                                Intent(this@LoginActivity, DepositAmmountActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "401")

                        Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", response.code().toString())
                        Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_LONG)
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

    fun forgotPassAPI() {
        Log.e("LOGIN", "START")
        Common.showLoadingProgress(this@LoginActivity)
        apiService = ApiClient.getClient()!!.create(ApiInterface::class.java)
        apiService!!.forgotPassword(forgotEmail.text.toString().trim())!!
            .enqueue(object : Callback<ForgotPasswordPojo?> {
                override fun onResponse(
                    call: Call<ForgotPasswordPojo?>,
                    response: Response<ForgotPasswordPojo?>
                ) {
                    when {
                        response.code() == 200 -> {
                            Common.dismissLoadingProgress()
                            rlForgot.visibility = View.GONE
                            val loginList: ForgotPasswordPojo? = response.body()
                          /*  Toast.makeText(
                                this@LoginActivity,
                                loginList!!.message,
                                Toast.LENGTH_LONG
                            ).show()*/

                            Log.e("LOGIN", "SUCCESS")

                        }
                        response.code() == 401 -> {
                            Common.dismissLoadingProgress()
                            Log.e("LOGINN", "401")

                            Toast.makeText(
                                this@LoginActivity,
                                response.message(),
                                Toast.LENGTH_LONG
                            )
                                .show()

                            return
                        }
                        response.code() == 400 -> {
                            Common.dismissLoadingProgress()
                            Log.e("LOGINN", "400")
                            Toast.makeText(
                                this@LoginActivity,
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
                                this@LoginActivity,
                                response.message(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            return
                        }
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordPojo?>, t: Throwable) {
                    Common.dismissLoadingProgress()
                    Log.e("LOGINN", "onFailure")
                    Log.d("TAG", "onFailure: " + t.message)
                }
            })
    }


}