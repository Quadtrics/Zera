package com.example.zeraapp.activitys.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zeraapp.R
import com.example.zeraapp.adapter.AddUserAdapter
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.AuthUsers.AuthUsersListRoot
import com.example.zeraapp.models.AuthUsers.AuthUsersDatum
import com.example.zeraapp.utlis.Common
import com.example.zeraapp.utlis.SharePreference
import com.example.zeraapp.utlis.SharePreference.Companion.getStringPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizedUserActivity : AppCompatActivity() {

    lateinit var addUserBtn:TextView
    lateinit var authTokenTxt:TextView
    lateinit var backAuthUser:ImageView
    lateinit var rv_authUser:RecyclerView
    var apiService: ApiInterface? = null
    private lateinit var addUserAdapter: AddUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorized_user)
        findId()

    }

    override fun onResume() {
        callAuthUser()
        super.onResume()
    }


    private fun findId() {
        rv_authUser = findViewById(R.id.rv_authUser)
        addUserBtn = findViewById(R.id.addUserBtn)
        backAuthUser = findViewById(R.id.backAuthUser)
        authTokenTxt = findViewById(R.id.authTokenTxt)
        clicks()
    }

    private fun clicks() {
        addUserBtn.setOnClickListener {
            var intent = Intent(this@AuthorizedUserActivity, AddUserActivity::class.java)
            startActivity(intent)
        }
        backAuthUser.setOnClickListener {
           onBackPressed()
        }

        callAuthUser()
    }



    private fun callAuthUser() {
        Common.showLoadingProgress(this@AuthorizedUserActivity)
        apiService = ApiClient.getClientToken(getStringPref(this@AuthorizedUserActivity, SharePreference.loginToken).toString())!!.create(ApiInterface::class.java)
        apiService!!.getAuthUser(getStringPref(this@AuthorizedUserActivity,SharePreference.userId).toString())!!.enqueue(object :
            Callback<AuthUsersListRoot?> {
            override fun onResponse(call: Call<AuthUsersListRoot?>, response: Response<AuthUsersListRoot?>) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val authUser: AuthUsersListRoot? = response.body()
                        callAddUserAdapter(authUser!!.data)
                        if (authUser!!.data!!.size == 0){
                            authTokenTxt.visibility = View.VISIBLE
                        }else{
                            authTokenTxt.visibility = View.GONE
                        }

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "401")

                        Toast.makeText(this@AuthorizedUserActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@AuthorizedUserActivity, MainActivity::class.java)
                        startActivity(intent)

                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(this@AuthorizedUserActivity, "Invalid Credentials", Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", response.code().toString())
                        Toast.makeText(this@AuthorizedUserActivity, response.message(), Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<AuthUsersListRoot?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("LOGINN", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }
        })
    }

    private fun callAddUserAdapter(data: List<AuthUsersDatum>?) {
        addUserAdapter = AddUserAdapter(this,data)
        val layoutManager = LinearLayoutManager(this)
        rv_authUser?.layoutManager = layoutManager
        rv_authUser?.itemAnimator = DefaultItemAnimator()
        rv_authUser?.adapter = addUserAdapter
    }

}