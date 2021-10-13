package com.example.zeraapp.activitys.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R

class FundAmmountActivity : AppCompatActivity() {
    lateinit var backHMC: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fund_ammount)
        findId()
    }

    private fun findId() {
        backHMC = findViewById(R.id.backFA)
        clicks()
    }

    private fun clicks() {
        backHMC.setOnClickListener {
            onBackPressed()
        }
    }
}