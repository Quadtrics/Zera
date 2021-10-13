package com.example.zeraapp.activitys.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.models.AuthUsersListRoot.AuthUserRoot
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
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

class AddUserActivity : AppCompatActivity() {
    lateinit var et_userName: EditText
    lateinit var et_userEmail: EditText
    lateinit var et_userMobile: EditText
    lateinit var et_userAddress: EditText
    lateinit var et_userState: EditText
    lateinit var et_userCity: EditText
    lateinit var ll_dateLayout: LinearLayout
    var dateSelected: String = ""
    lateinit var tv_dateDob: TextView
    lateinit var tv_monthDob: TextView
    lateinit var tv_yearDob: TextView
    var apiService: ApiInterface? = null


    lateinit var backAddUser: ImageView
    lateinit var tv_addUser: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        findid()
    }

    private fun findid() {

        tv_dateDob = findViewById(R.id.tv_dateDob)
        tv_monthDob = findViewById(R.id.tv_monthDob)
        tv_yearDob = findViewById(R.id.tv_yearDob)
        et_userName = findViewById(R.id.et_userName)
        et_userEmail = findViewById(R.id.et_userEmail)
        et_userMobile = findViewById(R.id.et_userMobile)
        et_userAddress = findViewById(R.id.et_userAddress)
        et_userState = findViewById(R.id.et_userState)
        et_userCity = findViewById(R.id.et_userCity)
        ll_dateLayout = findViewById(R.id.ll_dateLayout)


        backAddUser = findViewById(R.id.backAddUser)
        tv_addUser = findViewById(R.id.tv_addUser)

        clicks()
    }

    private fun clicks() {
        backAddUser.setOnClickListener {
            onBackPressed()
        }
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
        tv_addUser.setOnClickListener {
            if (getEtText(et_userName).equals("")) {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_name)
                )
            } else if (!Common.isValidEmail(getEtText(et_userEmail))) {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_email)
                )
            } else if (getEtText(et_userMobile).equals("")) {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_mobile)
                )

            } else if (getEtText(et_userMobile).length < 10 || getEtText(et_userMobile).length > 14) {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_mobile_length)
                )
            } else if (getEtText(et_userAddress).equals("")) {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_address)
                )
            } else if (getEtText(et_userState).equals("")) {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_state)
                )
            } else if (getEtText(et_userCity).equals("")) {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_city)
                )
            } else if (dateSelected == "" || dateSelected == "00-00-0000") {
                Common.showErrorFullMsg(
                    this@AddUserActivity,
                    resources.getString(R.string.validation_dateOfBirth)
                )
            } else {
                if (Common.isCheckNetwork(this@AddUserActivity)) {
                    addUserApi()
                } else {
                    Common.alertErrorOrValidationDialog(
                        this@AddUserActivity,
                        resources.getString(R.string.no_internet)
                    )
                }
            }
//            onBackPressed()
        }
    }

    private fun addUserApi() {
        Log.e("AddUser", "START")
        Common.showLoadingProgress(this@AddUserActivity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@AddUserActivity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.addAuthUser(
            SharePreference.getStringPref(this@AddUserActivity, SharePreference.userId).toString(),
            getEtText(et_userName),
            getEtText(et_userEmail),
            getEtText(et_userMobile),
            getEtText(et_userAddress),
            getEtText(et_userState),
            getEtText(et_userCity),
            dateSelected,
            "hksadlasldakjsdh",
            "android",
        ).enqueue(object :
            Callback<AuthUserRoot?> {
            override fun onResponse(
                call: Call<AuthUserRoot?>,
                response: Response<AuthUserRoot?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
//                            val authUsersList: AuthUsersListRoot? = response.body()
//                        showToast(this@AddUserActivity, response.message())
                        onBackPressed()
                        Log.e("AddUser", "SUCCESS")

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("AddUser", "401")

                        Toast.makeText(
                            this@AddUserActivity,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        val intent = Intent(this@AddUserActivity, MainActivity::class.java)
                        startActivity(intent)

                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("AddUser", "400")
                        Toast.makeText(
                            this@AddUserActivity,
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
                                    showToast(this@AddUserActivity, jsonObject.getString("message"))
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                            }

                        } else
                            Toast.makeText(
                                this@AddUserActivity,
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
                Log.e("AddUser", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }


        })
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
            DatePickerDialog(this@AddUserActivity, { view, year, monthOfYear, dayOfMonth ->
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