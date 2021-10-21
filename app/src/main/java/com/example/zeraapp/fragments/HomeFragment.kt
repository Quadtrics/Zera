package com.example.zeraapp.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zeraapp.R
import com.example.zeraapp.activitys.activity.AddFundActivity
import com.example.zeraapp.activitys.activity.DashBoardActivity
import com.example.zeraapp.activitys.activity.DepositAmmountActivity
import com.example.zeraapp.activitys.activity.MainActivity
import com.example.zeraapp.adapter.HomeTransactionAdapter
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.GetAuthUsers.AuthTransactions
import com.example.zeraapp.models.GetAuthUsers.TransactionPojo
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field


class HomeFragment : Fragment() {

    lateinit var addFund: RelativeLayout
    lateinit var tv_userNameHeader: TextView
    lateinit var tv_principalBalance: TextView
    lateinit var tv_incomingPayment: TextView
    lateinit var tv_currentBalance: TextView
    lateinit var tv_noTransactions: TextView
    lateinit var civ_profile: ImageView
    lateinit var iv_moreMenu: ImageView
    lateinit var rv_transaction: RecyclerView
    lateinit var svHome: ScrollView
    lateinit var transDots: ImageView
    private lateinit var transactionAdapter: HomeTransactionAdapter
    var apiService: ApiInterface? = null

    private lateinit var ctx: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        findId(view)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    private fun findId(view: View) {
        iv_moreMenu = view.findViewById(R.id.iv_moreMenu)
        civ_profile = view.findViewById(R.id.civ_profile)
        tv_userNameHeader = view.findViewById(R.id.tv_userNameHeader)
        tv_currentBalance = view.findViewById(R.id.tv_currentBalance)
        tv_noTransactions = view.findViewById(R.id.tv_noTransactions)
        tv_principalBalance = view.findViewById(R.id.tv_principalBalance)
        tv_incomingPayment = view.findViewById(R.id.tv_incomingPayment)
        addFund = view.findViewById(R.id.addFund)
        rv_transaction = view.findViewById(R.id.rv_transaction)
        svHome = view.findViewById(R.id.svHome)
        transDots = view.findViewById(R.id.transDots)

        clicks()

    }

    private fun setData(loginList: SignupPojo?) {
        setLog("DeviceCheck", loginList?.data!!.first_name.toString())
        SharePreference.setStringPref(
            ctx,
            SharePreference.userId,
            loginList.data!!.id
        )
        val isRenew = loginList.data!!.isRenew

        SharePreference.setStringPref(
            ctx,
            SharePreference.isRenew,
            isRenew.toString()
        )
        if (isRenew == 0) { // 0 = maturity_date is active
            iv_moreMenu.visibility = INVISIBLE
        }
        SharePreference.setStringPref(
            ctx,
            SharePreference.maturity_date,
            loginList.data!!.maturity_date.toString()
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
        var checkUserName: String? = loginList.data!!.user_name.toString()
        if (checkUserName?.isNotEmpty()!!) {
            SharePreference.setStringPref(
                ctx,
                SharePreference.userName, extractWord(loginList.data!!.user_name.toString())
            )
        } else {
            SharePreference.setStringPref(
                ctx,
                SharePreference.userName, extractWord(loginList.data!!.first_name.toString())
            )
        }
        SharePreference.setStringPref(
            ctx,
            SharePreference.userProfileImage,
            Common.BaseImageUrl + loginList.data!!.image.toString()
        )
        var varName = SharePreference.getStringPref(ctx, SharePreference.userName).toString()
        if (varName.isEmpty() || varName.equals(""))
            tv_userNameHeader.text =resources.getString(R.string.hello)+", " + extractWord(
                SharePreference.getStringPref(
                    ctx,
                    SharePreference.userFirstName
                ).toString())
        else
            tv_userNameHeader.text =resources.getString(R.string.hello)+", " + extractWord(
                SharePreference.getStringPref(
                    ctx,
                    SharePreference.userName
                ).toString())

        tv_currentBalance.text = makeDollars(
            SharePreference.getStringPref(ctx, SharePreference.userNetBalance).toString()
        )
        tv_principalBalance.text = makeDollars(
            SharePreference.getStringPref(ctx, SharePreference.principalBalance).toString()
        )
        tv_incomingPayment.text = makeDollars(
            SharePreference.getStringPref(ctx, SharePreference.incomingBalance).toString()
        )
        val imagePath: String? = SharePreference.getStringPref(
            ctx,
            SharePreference.userProfileImage
        )
        if (imagePath != null) {
            if (imagePath.isNotEmpty()) {
                Picasso.get()
                    .load(
                        imagePath
                    ).placeholder(R.drawable.app_icon)
                    .into(civ_profile)

            }
        }
        callTranactionsApi()


    }

    override fun onResume() {
        callTranactionsApi()
        super.onResume()
    }


    private fun clicks() {

        iv_moreMenu.setOnClickListener {
            showPopupMenus(iv_moreMenu)

        }
        addFund.setOnClickListener {
            var intent = Intent(activity, AddFundActivity::class.java)
            startActivity(intent)
        }
        transDots.setOnClickListener {
            var intent = Intent(activity, DashBoardActivity::class.java)
            intent.putExtra("nextTrans", "1")
            startActivity(intent)
        }

        svHome.fullScroll(ScrollView.FOCUS_UP)
        getUserInfo()
    }

    private fun showPopupMenus(view: View) {
        // inflate menu
        val wrapper: Context = ContextThemeWrapper(context, R.style.YOURSTYLE_PopupMenu)
        val popup = PopupMenu(wrapper, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.popup_men, popup.menu)
        val menuHelper: Any
        val argTypes: Array<Class<*>?>
        try {
            val fMenuHelper: Field = PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.isAccessible = true
            menuHelper = fMenuHelper.get(popup)
            argTypes = arrayOf(Boolean::class.javaPrimitiveType)
            menuHelper.javaClass.getDeclaredMethod("setForceShowIcon", *argTypes)
                .invoke(menuHelper, true)

        } catch (e: java.lang.Exception) {
        }

        popup.setOnMenuItemClickListener({ item: MenuItem? ->

            when (item!!.itemId) {
                R.id.im_renewAccount -> {
                    val intent = Intent(context, DepositAmmountActivity::class.java)
                    startActivity(intent)

/*
                    Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
*/
                }
                /*  R.id.header2 -> {
                      Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
                  }
                  R.id.header3 -> {
                      Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
                  }*/
            }

            true
        })
        popup.show()
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

    private fun callTranactionsApi() {
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                ctx,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.getPayments(
            SharePreference.getStringPref(
                ctx,
                SharePreference.userId
            ).toString(), 1, 5
        ).enqueue(object :
            Callback<AuthTransactions?> {
            override fun onResponse(
                call: Call<AuthTransactions?>,
                response: Response<AuthTransactions?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val authTransactions: AuthTransactions? = response.body()
                        if (authTransactions!!.data!!.size == 0) {
                            tv_noTransactions.visibility = View.VISIBLE
                        } else {
                            callTransactionAdapter(authTransactions.data)
                        }

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "401")

                        Toast.makeText(ctx, response.message(), Toast.LENGTH_LONG)
                            .show()

                        val intent = Intent(ctx, MainActivity::class.java)
                        startActivity(intent)
                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", "400")
                        Toast.makeText(ctx, "Invalid Credentials", Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("LOGINN", response.code().toString())
                        Toast.makeText(ctx, response.message(), Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<AuthTransactions?>, t: Throwable) {
                Common.dismissLoadingProgress()
                Log.e("LOGINN", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }
        })
    }


    private fun callTransactionAdapter(transactionList: List<TransactionPojo>?) {
        for(transaction in transactionList!!){
            transaction.description = replaceWord("Interest",
                transaction.description.toString(), requireContext().resources.getString(R.string.trans_interest))
            transaction.description = replaceWord("Open Account",
                transaction.description.toString(), requireContext().resources.getString(R.string.trans_open_Account))
            transaction.description = replaceWord("Referral",
                transaction.description.toString(), requireContext().resources.getString(R.string.trans_Referral))
            transaction.description = replaceWord("withDraw",
                transaction.description.toString(), requireContext().resources.getString(R.string.trans_Withdraw))
        }

        transactionAdapter = HomeTransactionAdapter(ctx, transactionList)
        val layoutManager = LinearLayoutManager(ctx)
        rv_transaction.layoutManager = layoutManager
        rv_transaction.itemAnimator = DefaultItemAnimator()
        rv_transaction.adapter = transactionAdapter

    }

}