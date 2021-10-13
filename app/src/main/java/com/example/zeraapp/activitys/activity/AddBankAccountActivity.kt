package com.example.zeraapp.activitys.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R

class
AddBankAccountActivity : AppCompatActivity() {
    lateinit var backAddCardBank:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bank_account)
        findId()
    }

    private fun findId() {
        backAddCardBank = findViewById(R.id.backAddCardBank)
        clicks()
    }

    private fun clicks() {
        backAddCardBank.setOnClickListener {
            onBackPressed()
        }
    }
}