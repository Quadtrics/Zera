package com.example.zeraapp.activitys.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.ChangePassword.ChangePasswordPojo
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.Common
import com.example.zeraapp.utlis.SharePreference
import com.example.zeraapp.utlis.SharePreference.Companion.getStringPref
import com.example.zeraapp.utlis.getEtText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var backChangePass:ImageView
    lateinit var changePassBtn:TextView
    lateinit var currentPass:EditText
    lateinit var newPass:EditText
    lateinit var confirmPass:EditText
    var apiService: ApiInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        findId()
    }

    private fun findId() {
        backChangePass = findViewById(R.id.backChangePass)
        changePassBtn = findViewById(R.id.changePassBtn)
        currentPass = findViewById(R.id.currentPass)
        newPass = findViewById(R.id.newPass)
        confirmPass = findViewById(R.id.confirmPass)

        clicks()
    }

    private fun clicks() {
        backChangePass.setOnClickListener {
            onBackPressed()
        }
        changePassBtn.setOnClickListener {
            if(currentPass.text.toString().equals("")){
                Common.showErrorFullMsg(this@ChangePasswordActivity,resources.getString(R.string.validation_oldpassword))
            }else if(newPass.text.toString().equals("")){
                Common.showErrorFullMsg(this@ChangePasswordActivity,resources.getString(R.string.validation_newpassword))
            }else if(getEtText(newPass).length<6){
                Common.showErrorFullMsg(this@ChangePasswordActivity,resources.getString(R.string.validation_valid_password))
            }else if(confirmPass.text.toString().equals("")){
                Common.showErrorFullMsg(this@ChangePasswordActivity,resources.getString(R.string.validation_cpassword))
            }else if(!getEtText(confirmPass).equals(getEtText(newPass))){
                Common.showErrorFullMsg(this@ChangePasswordActivity,resources.getString(R.string.validation_valid_cpassword))
            }else {
                changePassAPI()
            }
        }
    }

    fun changePassAPI() {
        Log.e("LOGIN", "START")
        Common.showLoadingProgress(this@ChangePasswordActivity)
        apiService = ApiClient.getClientToken(getStringPref(this@ChangePasswordActivity, SharePreference.loginToken).toString())!!.create(ApiInterface::class.java)
        apiService!!.changePassword(currentPass.text.toString(),newPass.text.toString(),confirmPass.text.toString())!!.enqueue(object : Callback<ChangePasswordPojo?> {
            override fun onResponse(call: Call<ChangePasswordPojo?>, response: Response<ChangePasswordPojo?>) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: ChangePasswordPojo? = response.body()
                        onBackPressed()
                        Log.e("LOGIN", "SUCCESS")

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "401")

                        Toast.makeText(this@ChangePasswordActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@ChangePasswordActivity, MainActivity::class.java)
                        startActivity(intent)


                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(this@ChangePasswordActivity, "Invalid Credentials", Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", response.code().toString())
                        Toast.makeText(this@ChangePasswordActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<ChangePasswordPojo?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("LOGINN", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }
        })
    }

}