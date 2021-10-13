package com.example.zeraapp.activitys.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.zeraapp.R

class SignContractActivity : AppCompatActivity() {
    lateinit var signUpContract:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_contract)

        findId()
    }

    private fun findId() {
        signUpContract = findViewById(R.id.signUpContract)

        clicks()
    }

    private fun clicks() {
        signUpContract.setOnClickListener {
            var intent = Intent(this@SignContractActivity,AddCardActivity::class.java)
            startActivity(intent)
        }
    }
}