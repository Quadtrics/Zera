package com.example.zeraapp.activitys.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.AuthUsers.AuthUsersDatum
import com.example.zeraapp.models.AuthUsers.AuthUsersListRoot
import com.example.zeraapp.models.AuthUsersListRoot.AuthUserRoot
import com.example.zeraapp.models.ChangePassword.ChangePasswordPojo
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class UpdateAuthUser : AppCompatActivity() {

    lateinit var backAuthorizeUser: ImageView

    lateinit var tv_deleteUser: TextView
    lateinit var tv_editUser: TextView
    lateinit var tv_authEmail: TextView
    lateinit var tv_monthDob: TextView
    lateinit var tv_dateDob: TextView
    lateinit var tv_yearDob: TextView
    lateinit var ll_dateLayout: LinearLayout
    lateinit var et_authUserName: EditText
    lateinit var et_authMobile: EditText
    lateinit var et_authAddress: EditText
    lateinit var et_userState: EditText
    lateinit var et_userCity: EditText
    var dateSelected: String = ""

    var apiService: ApiInterface? = null
    var authUserId: String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_authuser)
        findId()
    }

    private fun findId() {
        tv_yearDob = findViewById(R.id.tv_yearDob)
        tv_monthDob = findViewById(R.id.tv_monthDob)
        tv_dateDob = findViewById(R.id.tv_dateDob)
        ll_dateLayout = findViewById(R.id.ll_dateLayout)
        et_authUserName = findViewById(R.id.et_authUserName)
        tv_authEmail = findViewById(R.id.tv_authEmail)
        et_authMobile = findViewById(R.id.et_authMobile)
        et_authAddress = findViewById(R.id.et_authAddress)
        et_userState = findViewById(R.id.et_userState)
        et_userCity = findViewById(R.id.et_userCity)
        
        
        backAuthorizeUser = findViewById(R.id.backAuthorizeUser)
        tv_editUser = findViewById(R.id.tv_editUser)
        tv_deleteUser = findViewById(R.id.tv_deleteUser)
        clicks()
        authUserId = intent.getStringExtra("authUserId").toString()
        getUserInfo()


    }

    private fun getUserInfo() {
        Log.e("getAuthDetail", "START")
        Common.showLoadingProgress(this@UpdateAuthUser)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@UpdateAuthUser,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.getAuthUserDetail(
            authUserId!!
        )
            .enqueue(object :
                Callback<AuthUsersListRoot?> {
                override fun onResponse(call: Call<AuthUsersListRoot?>, response: Response<AuthUsersListRoot?>) {
                    when {
                        response.code() == 200 -> {
                            Common.dismissLoadingProgress()
                            val userList: AuthUsersListRoot? = response.body()
                            setData(userList)
                            Log.e("getAuthDetail", "SUCCESS")
                        }
                        response.code() == 401 -> {
                            Common.dismissLoadingProgress()
                            Log.e("getAuthDetail", "401")

                            Toast.makeText(
                                this@UpdateAuthUser,
                                response.message(),
                                Toast.LENGTH_LONG

                            )
                                .show()
                            val intent = Intent(this@UpdateAuthUser, MainActivity::class.java)
                            startActivity(intent)
                        }
                        response.code() == 400 -> {
                            Common.dismissLoadingProgress()
                            Log.e("getAuthDetail", "400")
                            Toast.makeText(
                                this@UpdateAuthUser,
                                "Invalid Credentials",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            return
                        }
                        else -> {
                            Common.dismissLoadingProgress()
                            Log.e("getAuthDetail", response.code().toString())
                            Toast.makeText(
                                this@UpdateAuthUser,
                                response.message(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            return
                        }
                    }
                }

                override fun onFailure(call: Call<AuthUsersListRoot?>, t: Throwable) {
                    Common.dismissLoadingProgress()
                    Log.e("getAuthDetail", "onFailure")
                    Log.d("TAG", "onFailure: " + t.message)
                }
            })
    }

    private fun setData(userList: AuthUsersListRoot?) {

        setLog("userAuthCheck", userList?.data!!.size.toString())
        if(userList?.data?.size!! >0){
            et_authUserName.setText(userList?.data!!.get(0).name.toString())
            tv_authEmail.setText(userList?.data!!.get(0).email.toString())
            et_authMobile.setText(userList?.data!!.get(0).phoneNumber.toString())
            et_authAddress.setText(userList?.data!!.get(0).address.toString())
            et_userState.setText(userList?.data!!.get(0).state.toString())
            et_userCity.setText(userList?.data!!.get(0).city.toString())
            dateSelected = userList?.data!!.get(0).dob.toString()
            setDateInText()


        }

    }
    private fun setDateInText() {
        if (dateSelected != "" && dateSelected != "00-00-0000") {
            val dateParts: List<String> = dateSelected.split("-")
            val day = dateParts[0]
            val month = dateParts[1]
            val year = dateParts[2]
            tv_dateDob.text = day
            tv_monthDob.text = month
            tv_yearDob.text = year

        } else {
            tv_dateDob.text = "dd"
            tv_monthDob.text = "mm"
            tv_yearDob.text = "yyyy"
        }
    }


    private fun clicks() {
        ll_dateLayout.setOnClickListener {
            if (dateSelected != "" && dateSelected != "00-00-0000") {
                val dateParts: List<String> = dateSelected.split("-")
                val day = dateParts[0]
                val month = dateParts[1]
                val year = dateParts[2]
                showDatePicker(day, month, year)
            } else {
                showDatePicker("", "", "")
            }
        }
        backAuthorizeUser.setOnClickListener {
            onBackPressed()
        }
        tv_deleteUser.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        tv_editUser.setOnClickListener {
            if (getEtText(et_authUserName).equals("")) {
                Common.showErrorFullMsg(
                    this@UpdateAuthUser,
                    resources.getString(R.string.validation_name)
                )
            } else if (getEtText(et_authMobile).equals("")) {
                Common.showErrorFullMsg(
                    this@UpdateAuthUser,
                    resources.getString(R.string.validation_mobile)
                )
            } else if (getEtText(et_authMobile).length < 10|| getEtText(et_authMobile).length > 14) {
                Common.showErrorFullMsg(
                    this@UpdateAuthUser,
                    resources.getString(R.string.validation_mobile_length)
                )
            } else if (getEtText(et_authAddress).equals("")) {
                Common.showErrorFullMsg(
                    this@UpdateAuthUser,
                    resources.getString(R.string.validation_address)
                )
            } else if (getEtText(et_userState).equals("")) {
                Common.showErrorFullMsg(
                    this@UpdateAuthUser,
                    resources.getString(R.string.validation_state)
                )
            } else if (getEtText(et_userCity).equals("")) {
                Common.showErrorFullMsg(
                    this@UpdateAuthUser,
                    resources.getString(R.string.validation_city)
                )
            } else if (dateSelected == "" || dateSelected == "00-00-0000") {
                Common.showErrorFullMsg(
                    this@UpdateAuthUser,
                    resources.getString(R.string.validation_dateOfBirth)
                )
            } else {
                if (Common.isCheckNetwork(this@UpdateAuthUser)) {
                    updateUserInfo()
                } else {
                    Common.alertErrorOrValidationDialog(
                        this@UpdateAuthUser,
                        resources.getString(R.string.no_internet)
                    )
                }


            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_user_layout, null)
        val customDialog = AlertDialog.Builder(this@UpdateAuthUser)
            .setView(dialogView)
            .show()
        val rl_ok = dialogView.findViewById<RelativeLayout>(R.id.rl_ok)
        rl_ok.setOnClickListener {
            hitDeleteUserApi();
            customDialog.dismiss()
        }
        val rl_cancel = dialogView.findViewById<RelativeLayout>(R.id.rl_cancel)
        rl_cancel.setOnClickListener {
            customDialog.dismiss()
        }

    }



    private fun hitDeleteUserApi() {
        Log.e("Del_Auth", "START")
        Common.showLoadingProgress(this@UpdateAuthUser)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@UpdateAuthUser,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.deleteAuthUser(
            authUserId
        )!!.enqueue(object :
            Callback<ChangePasswordPojo?> {
            override fun onResponse(
                call: Call<ChangePasswordPojo?>,
                response: Response<ChangePasswordPojo?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: ChangePasswordPojo? = response.body()
                        
                        onBackPressed()
                        Log.e("Del_Auth", "SUCCESS")

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("Del_Auth", "401")
                        showToast(this@UpdateAuthUser,response.message())
                        val intent = Intent(this@UpdateAuthUser, MainActivity::class.java)
                        startActivity(intent)

                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("Del_Auth", "400")
                        Toast.makeText(
                            this@UpdateAuthUser,
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
                                    showToast(this@UpdateAuthUser, jsonObject.getString("message"))
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                            }

                        } else
                            Toast.makeText(
                                this@UpdateAuthUser,
                                response.message(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        return
                    }

                }
            }

            override fun onFailure(call: Call<ChangePasswordPojo?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("Del_Auth", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }


        })
    }
    private fun updateUserInfo() {
        Log.e("UpdateAuth", "START")
        Common.showLoadingProgress(this@UpdateAuthUser)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@UpdateAuthUser,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.userAuthUpdate(
            authUserId,
            getEtText(et_authUserName),
            getEtText(et_authMobile),
//            Integer.parseInt(getEtText(et_authMobile)),
            getEtText(et_userState),
            getEtText(et_userCity),
            dateSelected,
            getEtText(et_authAddress)
        )!!.enqueue(object :
            Callback<AuthUserRoot?> {
            override fun onResponse(
                call: Call<AuthUserRoot?>,
                response: Response<AuthUserRoot?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: AuthUserRoot? = response.body()

                        onBackPressed()
                        Log.e("UpdateAuth", "SUCCESS")

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("UpdateAuth", "401")

                        Toast.makeText(
                            this@UpdateAuthUser,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        val intent = Intent(this@UpdateAuthUser, MainActivity::class.java)
                        startActivity(intent)

                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("UpdateAuth", "400")
                        Toast.makeText(
                            this@UpdateAuthUser,
                            "Invalid Credentials",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("UpdateAuth", response.code().toString())
                        Toast.makeText(
                            this@UpdateAuthUser,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<AuthUserRoot?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("UpdateAuth", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }


        })
    }


    private fun showDatePicker(prevDay: String, prevMonth: String, prevYear: String) {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        if (prevDay.isEmpty() && prevMonth.isEmpty() || prevYear.isEmpty()) {
            //do nothing
        } else {
            year = Integer.parseInt(prevYear)
            month = Integer.parseInt(prevMonth) - 1
            day = Integer.parseInt(prevDay)
        }
        val dpd =
            DatePickerDialog(this@UpdateAuthUser, { view, year, monthOfYear, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal[Calendar.YEAR] = year
                cal[Calendar.MONTH] = monthOfYear
                cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                cal[Calendar.HOUR_OF_DAY] = 0
                cal[Calendar.MINUTE] = 0
                cal[Calendar.SECOND] = 0
                cal.set(Calendar.MILLISECOND, 0)
                val date = cal.time
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                dateSelected = sdf.format(date)
                setDateInText()
                /* setLog("MyNewDate","Da"+cal.time)
                 setLog("MyNewDate","Da   "+            sdf.format(date)
                 )*/
            }, year, month, day)
        dpd.datePicker.maxDate = Date().time


        dpd.show()

    }
}