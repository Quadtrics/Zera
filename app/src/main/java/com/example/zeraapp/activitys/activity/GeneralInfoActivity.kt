package com.example.zeraapp.activitys.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import com.example.zeraapp.utlis.SharePreference.Companion.getStringPref
import com.example.zeraapp.utlis.SharePreference.Companion.userId
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

class GeneralInfoActivity : AppCompatActivity() {

    lateinit var backGeneral: ImageView
    lateinit var saveInfo: TextView
    lateinit var tv_dateDob: TextView
    lateinit var tv_monthDob: TextView
    lateinit var tv_yearDob: TextView
    lateinit var et_userName: EditText
    lateinit var tv_userEmail: TextView
    lateinit var et_userMobile: EditText
    lateinit var et_userAddress: EditText
    lateinit var et_userState: EditText
    lateinit var et_userCity: EditText
    lateinit var ll_dateLayout: LinearLayout
    var dateSelected: String = ""

    var apiService: ApiInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_info)
        findId()
    }

    private fun findId() {
        backGeneral = findViewById(R.id.backGeneral)
        saveInfo = findViewById(R.id.saveInfo)

        et_userName = findViewById(R.id.et_userName)
        tv_userEmail = findViewById(R.id.tv_userEmail)
        et_userMobile = findViewById(R.id.et_userMobile)
        et_userAddress = findViewById(R.id.et_userAddress)
        et_userCity = findViewById(R.id.et_userCity)
        et_userState = findViewById(R.id.et_userState)
        tv_dateDob = findViewById(R.id.tv_dateDob)
        tv_monthDob = findViewById(R.id.tv_monthDob)
        tv_yearDob = findViewById(R.id.tv_yearDob)
        ll_dateLayout = findViewById(R.id.ll_dateLayout)
        getUserInfo()
        clics()
    }

    private fun clics() {
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
        backGeneral.setOnClickListener {
            onBackPressed()
        }
        saveInfo.setOnClickListener {
            if (getEtText(et_userName).equals("")) {
                Common.showErrorFullMsg(
                    this@GeneralInfoActivity,
                    resources.getString(R.string.validation_name)
                )
            } else if (getEtText(et_userMobile).equals("")) {
                Common.showErrorFullMsg(
                    this@GeneralInfoActivity,
                    resources.getString(R.string.validation_mobile)
                )
            } else if (getEtText(et_userMobile).length < 10|| getEtText(et_userMobile).length > 14) {
                Common.showErrorFullMsg(
                    this@GeneralInfoActivity,
                    resources.getString(R.string.validation_mobile_length)
                )
            } else if (getEtText(et_userAddress).equals("")) {
                Common.showErrorFullMsg(
                    this@GeneralInfoActivity,
                    resources.getString(R.string.validation_address)
                )
            } else if (getEtText(et_userState).equals("")) {
                Common.showErrorFullMsg(
                    this@GeneralInfoActivity,
                    resources.getString(R.string.validation_state)
                )
            } else if (getEtText(et_userCity).equals("")) {
                Common.showErrorFullMsg(
                    this@GeneralInfoActivity,
                    resources.getString(R.string.validation_city)
                )
            } else if (dateSelected == "" || dateSelected == "00-00-0000") {
                Common.showErrorFullMsg(
                    this@GeneralInfoActivity,
                    resources.getString(R.string.validation_dateOfBirth)
                )
            } else {
                if (Common.isCheckNetwork(this@GeneralInfoActivity)) {
                    updateUserInfo()
                } else {
                    Common.alertErrorOrValidationDialog(
                        this@GeneralInfoActivity,
                        resources.getString(R.string.no_internet)
                    )
                }


            }
        }
    }

    private fun updateUserInfo() {
        Log.e("LOGIN", "START")
        Common.showLoadingProgress(this@GeneralInfoActivity)
        apiService = ApiClient.getClientToken(
            getStringPref(
                this@GeneralInfoActivity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.userUpdate(
            getStringPref(this@GeneralInfoActivity, userId).toString(),
            getEtText(et_userName),
            tv_userEmail.text.toString(),
            getEtText(et_userMobile),
            getEtText(et_userState),
            getEtText(et_userCity),
            dateSelected,
            getEtText(et_userAddress), "3"
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
                        SharePreference.setStringPref(
                            this@GeneralInfoActivity,
                            SharePreference.userMobile,
                            getEtText(et_userMobile)
                        )
                        SharePreference.setStringPref(
                            this@GeneralInfoActivity,
                            SharePreference.userName,
                            extractWord(getEtText(et_userName))
                        )

                        onBackPressed()
                        Log.e("LOGIN", "SUCCESS")

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "401")

                        Toast.makeText(
                            this@GeneralInfoActivity,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        val intent = Intent(this@GeneralInfoActivity, MainActivity::class.java)
                        startActivity(intent)

                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(
                            this@GeneralInfoActivity,
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
                                    showToast(this@GeneralInfoActivity, jsonObject.getString("message"))
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                            }

                        } else
                            Toast.makeText(
                                this@GeneralInfoActivity,
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

    private fun getUserInfo() {
        Log.e("LOGIN", "START")
        Common.showLoadingProgress(this@GeneralInfoActivity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@GeneralInfoActivity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.getUser(getStringPref(this@GeneralInfoActivity, userId).toString())
            .enqueue(object :
                Callback<SignupPojo?> {
                override fun onResponse(call: Call<SignupPojo?>, response: Response<SignupPojo?>) {
                    when {
                        response.code() == 200 -> {
                            Common.dismissLoadingProgress()
                            val loginList: SignupPojo? = response.body()
                            setData(loginList)
                            Log.e("LOGIN", "SUCCESS")
                        }
                        response.code() == 401 -> {
                            Common.dismissLoadingProgress()
                            Log.e("LOGINN", "401")

                            Toast.makeText(
                                this@GeneralInfoActivity,
                                response.message(),
                                Toast.LENGTH_LONG

                            )
                                .show()
                            val intent = Intent(this@GeneralInfoActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        response.code() == 400 -> {
                            Common.dismissLoadingProgress()
                            Log.e("LOGINN", "400")
                            Toast.makeText(
                                this@GeneralInfoActivity,
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
                                this@GeneralInfoActivity,
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

    private fun setData(loginList: SignupPojo?) {
        setLog("DeviceCheck", loginList?.data!!.first_name.toString())
        et_userName.setText(loginList.data!!.user_name.toString())
        tv_userEmail.text = loginList.data!!.email.toString()
        et_userMobile.setText(loginList.data!!.phoneNumber.toString())
        et_userAddress.setText(loginList.data!!.address.toString())
        et_userCity.setText(loginList.data!!.city.toString())
        et_userState.setText(loginList.data!!.state.toString())
        dateSelected = loginList.data!!.dob.toString()
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            userId,
            loginList.data!!.id
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.loginToken,
            loginList.data!!.token
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userFirstName,
            loginList.data!!.first_name
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userLastName,
            loginList.data!!.last_name
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userEmail,
            loginList.data!!.email
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userNetBalance,
            loginList.data!!.net_balance.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userNetEarn,
            loginList.data!!.net_earned.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userNetInterest,
            loginList.data!!.net_interest.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.incomingBalance,
            loginList.data!!.incoming.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.principalBalance,
            loginList.data!!.principle_balance.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userMobile,
            loginList.data!!.phoneNumber.toString()
        )
        val isRenew = loginList.data!!.isRenew

        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.isRenew,
            isRenew.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.clientId,
            loginList.data!!.client_id.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userName,
            loginList.data!!.user_name.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.maturity_date,
            loginList.data!!.maturity_date.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.contract,
            loginList.data!!.contract.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.isFirstDeposit,
            loginList.data!!.isFirstDeposit.toString()
        )
        SharePreference.setStringPref(
            this@GeneralInfoActivity,
            SharePreference.userProfileImage,
            Common.BaseImageUrl + loginList.data!!.image.toString()
        )

        setDateInText()

/*
        SharePreference.setStringPref(
            this@LoginActivity,
            SharePreference.userId,
            loginList!!.data!!.id
        )
*/

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
            DatePickerDialog(this@GeneralInfoActivity, { view, year, monthOfYear, dayOfMonth ->
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