package com.example.zeraapp.activitys.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zeraapp.R
import com.example.zeraapp.adapter.EarningAdapter
import com.example.zeraapp.models.EarningPojo
import com.example.zeraapp.utlis.makeDollars
import com.example.zeraapp.utlis.replaceWord

class EarningWithActivity : AppCompatActivity() {
    lateinit var RvEarnings: RecyclerView
    lateinit var backEarningWith: ImageView
    lateinit var tv_principalAmount: TextView
    lateinit var tv_allDone: TextView
    lateinit var tv_timePeriod: TextView
    lateinit var tv_totalEarned: TextView
    lateinit var tv_withdraw: TextView
    lateinit var tv_paymentCount: TextView
    private lateinit var earningAdapter: EarningAdapter
    var principalBalance: String = ""
    var termSelected: String = ""
    var termCount: String = ""
    var pAmount = 0.0
    var init_pAmount = 0.0
    var time = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earning_with)
        findId()
    }

    /*
        * public class MyClass {
        public static void main(String args[]) {
            double sum=500.0;
            for(int i=1;i<=12;i++){
                double amount=calcInterest(sum);
                double newAmount=sum+amount;
                System.out.println(sum+" "+i+" "+amount+" "+newAmount);
                sum = newAmount;

            }
        }
        public static Double calcInterest(double p){
            double interest = p*5/100;
            return interest;
        }
    }
        * */
    private fun findId() {
        RvEarnings = findViewById(R.id.RvEarnings)
        backEarningWith = findViewById(R.id.backEarningWith)
        tv_principalAmount = findViewById(R.id.tv_principalAmount)
        tv_allDone = findViewById(R.id.tv_allDone)
        tv_timePeriod = findViewById(R.id.tv_timePeriod)
        tv_totalEarned = findViewById(R.id.tv_totalEarned)
        tv_withdraw = findViewById(R.id.tv_withdraw)
        tv_paymentCount = findViewById(R.id.tv_paymentCount)
        clicks()
        getData()
    }

    private fun getData() {
        principalBalance = intent.getStringExtra("principalAmount").toString()
        termCount = intent.getStringExtra("termCount").toString()
        termSelected = intent.getStringExtra("term").toString()
        tv_principalAmount.text = makeDollars(principalBalance)
        var displayTimePeriod= termCount + " " + termSelected
        tv_timePeriod.text =  replaceWord(displayTimePeriod,"month",getString(R.string.months))
        tv_timePeriod.text =  replaceWord(displayTimePeriod,"year",getString(R.string.years))
        if (termSelected.equals("year")) {
            time = Integer.parseInt(termCount) * 12
        } else {
            time = Integer.parseInt(termCount)
        }
        pAmount = principalBalance.toDouble()
        init_pAmount = principalBalance.toDouble()
        initiateCalculation()
    }

    private fun initiateCalculation() {
        val data = ArrayList<EarningPojo>()

        for (i in 1..time) {
            val interestEarned: Double = String.format("%.2f", pAmount * 0.05).toDouble()
            val newAmount = String.format("%.2f", pAmount + interestEarned).toDouble()

            data.add(
                EarningPojo(
                    Integer.toString(i),
                    makeDollars(pAmount.toString()),
                    makeDollars(interestEarned.toString()),
                    makeDollars(newAmount.toString())
                )
            )
            pAmount = newAmount
        }
        tv_paymentCount.setText(time.toString())
        tv_totalEarned.text = makeDollars(String.format("%.2f", pAmount - init_pAmount))
        tv_withdraw.text = makeDollars(String.format("%.2f", pAmount))
        earningAdapter = EarningAdapter(this, data)
        val layoutManager = LinearLayoutManager(this)
        RvEarnings.layoutManager = layoutManager
        RvEarnings.itemAnimator = DefaultItemAnimator()
        RvEarnings.adapter = earningAdapter

    }

    private fun clicks() {
        backEarningWith.setOnClickListener {
            onBackPressed()
        }
        tv_allDone.setOnClickListener {
            onBackPressed()
        }
    }

}