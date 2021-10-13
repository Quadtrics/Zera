package com.example.zeraapp.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.zeraapp.R
import com.example.zeraapp.activitys.activity.*
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WalletFragment : Fragment() {

    lateinit var withdraw:RelativeLayout
    lateinit var redeposit:RelativeLayout
    lateinit var tv_amountWithdraw: TextView
    lateinit var tv_principalBalance: TextView
    lateinit var tv_incomingPayment: TextView
    lateinit var tv_totalEarned: TextView
    lateinit var tv_maturityDate: TextView
    var apiService: ApiInterface? = null
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_wallet, container, false)


        findId(view)
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }
    private fun findId(view: View) {
        withdraw = view.findViewById(R.id.withdraw)
        redeposit = view.findViewById(R.id.redeposit)
        tv_amountWithdraw = view.findViewById(R.id.tv_amountWithdraw)
        tv_maturityDate = view.findViewById(R.id.tv_maturityDate)
        tv_principalBalance = view.findViewById(R.id.tv_principalBalance)
        tv_incomingPayment = view.findViewById(R.id.tv_incomingPayment)
        tv_totalEarned = view.findViewById(R.id.tv_totalEarned)


        clicks()

    }

    private fun clicks() {
        withdraw.setOnClickListener {
            var intent = Intent(activity,WithdrawActivity::class.java)
            startActivity(intent)
        }
        redeposit.setOnClickListener {
            var intent = Intent(activity,RedepositActivity::class.java)
            startActivity(intent)
        }
        getUserInfo()
    }
    override fun onResume() {
        getUserInfo()
        super.onResume()
    }


    private fun getUserInfo() {
        Log.e("LOGIN", "START")
        Common.showLoadingProgress(context as Activity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                ctx,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.getUser(
            SharePreference.getStringPref(
                ctx,
                SharePreference.userId
            ).toString()
        )
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
                                ctx,
                                response.message(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            val intent = Intent(ctx, MainActivity::class.java)
                            startActivity(intent)
                        }
                        response.code() == 400 -> {
                            Common.dismissLoadingProgress()
                            Log.e("LOGINN", "400")
                            Toast.makeText(
                                ctx,
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
                                ctx,
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
        SharePreference.setStringPref(
            ctx,
            SharePreference.userId,
            loginList.data!!.id
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.loginToken,
            loginList.data!!.token
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userFirstName,
            loginList.data!!.first_name
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userLastName,
            loginList.data!!.last_name
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userEmail,
            loginList.data!!.email
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userNetBalance,
            loginList.data!!.net_balance.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userNetEarn,
            loginList.data!!.net_earned.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userNetInterest,
            loginList.data!!.net_interest.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.incomingBalance,
            loginList.data!!.incoming.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.principalBalance,
            loginList.data!!.principle_balance.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userMobile,
            loginList.data!!.phoneNumber.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.clientId,
            loginList.data!!.client_id.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userName,
            loginList.data!!.user_name.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.maturity_date,
            loginList.data!!.maturity_date.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.contract,
            loginList.data!!.contract.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.isFirstDeposit,
            loginList.data!!.isFirstDeposit.toString()
        )
        SharePreference.setStringPref(
            ctx,
            SharePreference.userProfileImage,
            Common.BaseImageUrl + loginList.data!!.image.toString()
        )
        val isRenew = loginList.data!!.isRenew

        SharePreference.setStringPref(
            ctx,
            SharePreference.isRenew,
            isRenew.toString()
        )

        tv_amountWithdraw.text = makeDollars(
            SharePreference.getStringPref(ctx, SharePreference.userNetInterest).toString()
        )
        tv_principalBalance.text = makeDollars(
            SharePreference.getStringPref(ctx, SharePreference.principalBalance).toString()
        )
        tv_incomingPayment.text = makeDollars(
            SharePreference.getStringPref(ctx, SharePreference.incomingBalance).toString()
        )
        tv_totalEarned.text = makeDollars(
            SharePreference.getStringPref(ctx, SharePreference.userNetEarn).toString()
        )
        tv_maturityDate.text = convertDate(SharePreference.getStringPref(ctx, SharePreference.maturity_date).toString(),
            "yyyy-MM-dd",
        "MM/dd/yyyy")



    }



}