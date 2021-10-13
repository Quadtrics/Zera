package com.example.zeraapp.activitys.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import com.example.zeraapp.utlis.SharePreference.Companion.loginToken
import com.example.zeraapp.utlis.SharePreference.Companion.setStringPref
import com.example.zeraapp.utlis.SharePreference.Companion.userId
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class SignUpActivity : AppCompatActivity() {
    lateinit var et_firstName: EditText
    lateinit var et_lastName: EditText
    lateinit var et_phoneNumber: EditText
    lateinit var et_email: EditText
    lateinit var et_password: EditText
    lateinit var rl_termsCheckBox: RelativeLayout
    lateinit var tv_signUp: TextView
    lateinit var iv_check: ImageView
    var termsApproved = false

    lateinit var tv_loginSign: TextView
    lateinit var terms: TextView
    lateinit var policy: TextView
    lateinit var privacyNotice: TextView
    lateinit var signBack: ImageView
    lateinit var passHide: ImageView
    var apiService: ApiInterface? = null
    lateinit var passTogel: RelativeLayout
    var isShow: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        findID()
    }

    private fun findID() {
        tv_signUp = findViewById(R.id.tv_signUp)
        tv_loginSign = findViewById(R.id.tv_loginSign)
        terms = findViewById(R.id.terms)
        policy = findViewById(R.id.policy)
        privacyNotice = findViewById(R.id.policy)
        signBack = findViewById(R.id.signBack)
        iv_check = findViewById(R.id.iv_check)
        rl_termsCheckBox = findViewById(R.id.rl_termsCheckBox)
        passTogel = findViewById(R.id.passTogel)
        passHide = findViewById(R.id.passHide)
        et_password = findViewById(R.id.et_password)

        et_firstName = findViewById(R.id.et_firstName)
        et_lastName = findViewById(R.id.et_lastName)
        et_phoneNumber = findViewById(R.id.et_phoneNumber)
        et_email = findViewById(R.id.et_email)

        click()
    }

    private fun click() {

        tv_signUp.setOnClickListener {
            if (getEtText(et_firstName).isEmpty()) {
                Common.showErrorFullMsg(
                    this@SignUpActivity,
                    resources.getString(R.string.validation_firstname)
                )
            } else if (getEtText(et_lastName).isEmpty()) {
                Common.showErrorFullMsg(
                    this@SignUpActivity,
                    resources.getString(R.string.validation_lastname)
                )
            } else if (getEtText(et_phoneNumber).length < 10 || getEtText(et_phoneNumber).length > 14) {
                Common.showErrorFullMsg(
                    this@SignUpActivity,
                    resources.getString(R.string.validation_mobile_length)
                )
            } else if (!Common.isValidEmail(getEtText(et_email))) {
                Common.showErrorFullMsg(
                    this@SignUpActivity,
                    resources.getString(R.string.validation_email)
                )
            } else if (getEtText(et_password).isEmpty()) {
                Common.showErrorFullMsg(
                    this@SignUpActivity,
                    resources.getString(R.string.validation_password)
                )
            } else if (getEtText(et_password).length < 6) {
                Common.showErrorFullMsg(
                    this@SignUpActivity,
                    resources.getString(R.string.validation_valid_password)
                )
            } else if (!termsApproved) {
                Common.showErrorFullMsg(
                    this@SignUpActivity,
                    resources.getString(R.string.validation_termsCondtions)
                )
            } else {
                if (Common.isCheckNetwork(this@SignUpActivity)) {
                    callSignUp()
                } else {
                    Common.alertErrorOrValidationDialog(
                        this@SignUpActivity,
                        resources.getString(R.string.no_internet)
                    )
                }


            }

        }
        tv_loginSign.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        terms.setOnClickListener {
//            val intent = Intent(this@SignUpActivity, SignUpActivity::class.java)
//            startActivity(intent)
        }
        policy.setOnClickListener {
//            val intent = Intent(this@SignUpActivity, SignUpActivity::class.java)
//            startActivity(intent)
        }
        privacyNotice.setOnClickListener {
//            val intent = Intent(this@SignUpActivity, SignUpActivity::class.java)
//            startActivity(intent)
        }
        signBack.setOnClickListener {
            onBackPressed()
        }
        rl_termsCheckBox.setOnClickListener {
            if (termsApproved) {
                iv_check.visibility = INVISIBLE
                termsApproved = false
            } else {
                iv_check.visibility = VISIBLE
                termsApproved = true
            }
        }
        passTogel.setOnClickListener {
            passHide.visibility = if (passHide.visibility === View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }

            if (isShow == true) {
                et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                isShow = false
            } else {
                et_password.transformationMethod = PasswordTransformationMethod.getInstance()
                isShow = true
            }

        }

    }


    private fun callSignUp() {
        Common.showLoadingProgress(this@SignUpActivity)
        apiService = ApiClient.getClient()!!.create(ApiInterface::class.java)
        apiService!!.signUp(
            getEtText(et_firstName),
            getEtText(et_lastName),
            getEtText(et_email),
            getEtText(et_password),
            getEtText(et_phoneNumber),
            "android",
            "qwe"
        )!!.enqueue(object : Callback<SignupPojo?> {
            override fun onResponse(call: Call<SignupPojo?>, response: Response<SignupPojo?>) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: SignupPojo? = response.body()
                        Log.e("LOGIN", "SUCCESS")
                        setLog("SignUp_response", loginList.toString())
                        setStringPref(this@SignUpActivity, userId, loginList!!.data!!.id)
                        setStringPref(this@SignUpActivity, loginToken, loginList.data!!.token)
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userFirstName,
                            loginList.data!!.first_name
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userLastName,
                            loginList.data!!.last_name
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userEmail,
                            loginList.data!!.email
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userNetBalance,
                            loginList.data!!.net_balance.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userNetEarn,
                            loginList.data!!.net_earned.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userNetInterest,
                            loginList.data!!.net_interest.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.incomingBalance,
                            loginList.data!!.incoming.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.principalBalance,
                            loginList.data!!.principle_balance.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userMobile,
                            loginList.data!!.phoneNumber.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.clientId,
                            loginList.data!!.client_id.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.userName,
                            extractWord(loginList.data!!.first_name.toString())
                        )

                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.maturity_date,
                            loginList.data!!.maturity_date.toString()
                        )
                        setStringPref(
                            this@SignUpActivity,
                            SharePreference.maturity_date,
                            loginList.data!!.maturity_date.toString()
                        )
                        val intent = Intent(this@SignUpActivity, DepositAmmountActivity::class.java)

                        startActivity(intent)
                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "401")
                        Toast.makeText(this@SignUpActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(
                            this@SignUpActivity,
                            "Invalid Credentials",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", response.code().toString())
                        if (response.code() == 409) {
                            if (!response.isSuccessful) {
                                val i: InputStream = response.errorBody()!!.byteStream()
                                val r = BufferedReader(InputStreamReader(i))
                                val errorResult = StringBuilder()
                                var line: String?
                                Log.e("CheckErrorCode", i.toString())

                                try {
                                    while (r.readLine().also { line = it } != null) {
                                        errorResult.append(line).append('\n')
                                    }
                                    Log.e("CheckErrorCode", errorResult.toString())
                                    val jsonObject = JSONObject(errorResult.toString())
                                    Log.e("CheckErrorCode", jsonObject.getString("message"))
                                    showToast(this@SignUpActivity, jsonObject.getString("message"))
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                            }

                        } else
                            Toast.makeText(
                                this@SignUpActivity,
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


