package com.example.zeraapp.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zeraapp.R
import com.example.zeraapp.activitys.activity.MainActivity
import com.example.zeraapp.adapter.HomeTransactionAdapter
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.GetAuthUsers.AuthTransactions
import com.example.zeraapp.models.GetAuthUsers.TransactionPojo
import com.example.zeraapp.utlis.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionFragment : Fragment() {

    lateinit var RvTransaction: RecyclerView
    private lateinit var transactionAdapter: HomeTransactionAdapter
    private lateinit var ctx: Context
    var apiService: ApiInterface? = null
    lateinit var tv_noTransactions: TextView
    lateinit var et_searchTransaction: EditText
    var transactionList: List<TransactionPojo>? = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_transaction, container, false)
        findId(view)
    return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }
    private fun findId(view: View) {
        RvTransaction = view.findViewById(R.id.RvTransaction)
        tv_noTransactions = view.findViewById(R.id.tv_noTransactions)
        et_searchTransaction = view.findViewById(R.id.et_searchTransaction)
        setListener()
        callTranactionsApi()
    }
    override fun onResume() {
        callTranactionsApi()
        super.onResume()
    }
    private fun setListener() {
        et_searchTransaction.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                getFilteredTransactions(getEtText(et_searchTransaction))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                getFilteredTransactions(getEtText(et_searchTransaction))

            }
        })
    }

    private fun getFilteredTransactions(etText: String) {
        val filteredTransactions: ArrayList<TransactionPojo> = ArrayList()
        if(etText.isNotEmpty()) {
            for (transaction in transactionList!!) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                    setLog("TransactionCheck"," "+transaction.description)
                if (transaction.description.toString().contains(etText, ignoreCase = true)) {
                    filteredTransactions.add(transaction)
                }else if (transaction.date.toString().contains(etText, ignoreCase = true)) {
                    filteredTransactions.add(transaction)
                }else if (transaction.amount.toString().contains(etText, ignoreCase = true)) {
                    filteredTransactions.add(transaction)
                }
            }
            if(filteredTransactions.size==0){
                tv_noTransactions.setVisibility(View.VISIBLE)
            }else{
                tv_noTransactions.setVisibility(View.GONE)
            }
            transactionAdapter.updateList(filteredTransactions)

        }else{
            transactionAdapter.updateList(transactionList)
            tv_noTransactions.setVisibility(View.GONE)

        }
        //update recyclerview
        //update recyclerview
//        disp_adapter.updateList(filteredTransactions)


    }

    private fun callTranactionsApi() {
        Common.showLoadingProgress(context as Activity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                ctx,
                SharePreference.loginToken
            ).toString())!!.create(ApiInterface::class.java)
        apiService!!.getPayments(
            SharePreference.getStringPref(
                ctx,
                SharePreference.userId
            ).toString(),1,100)!!.enqueue(object :
            Callback<AuthTransactions?> {
            override fun onResponse(call: Call<AuthTransactions?>, response: Response<AuthTransactions?>) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val authTransactions: AuthTransactions? = response.body()
                        if (authTransactions!!.data!!.size == 0){
                            tv_noTransactions.visibility = View.VISIBLE
                        }else{
                            transactionList = authTransactions!!.data
                            callHomeTransactionAdapter()
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



    private fun callHomeTransactionAdapter() {
        for(transaction in transactionList!!){
            transaction.description = replaceWord("Interest",
                transaction.description.toString(), resources.getString(R.string.trans_interest))
            transaction.description = replaceWord("Open Account",
                transaction.description.toString(), resources.getString(R.string.trans_open_Account))
            transaction.description = replaceWord("Referral",
                transaction.description.toString(), resources.getString(R.string.trans_Referral))
            transaction.description = replaceWord("withDraw",
                transaction.description.toString(), resources.getString(R.string.trans_Withdraw))
        }
        transactionAdapter = HomeTransactionAdapter(ctx,transactionList)
        val layoutManager = LinearLayoutManager(ctx)
        RvTransaction.layoutManager = layoutManager
        RvTransaction.itemAnimator = DefaultItemAnimator()
        RvTransaction.adapter = transactionAdapter

    }


}