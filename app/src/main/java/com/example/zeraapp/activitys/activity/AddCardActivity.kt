package com.example.zeraapp.activitys.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.zeraapp.R

class AddCardActivity : AppCompatActivity() {
    lateinit var backAddCard:ImageView
    lateinit var addCardBtn:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        findId()
    }

    private fun findId() {
        backAddCard = findViewById(R.id.backAddCard)
        addCardBtn = findViewById(R.id.addCardBtn)

        clicks()
    }

    private fun clicks() {
        backAddCard.setOnClickListener {
            onBackPressed()
        }
        addCardBtn.setOnClickListener {
           var intent = Intent(this@AddCardActivity , DashBoardActivity::class.java)
            startActivity(intent)
        }
    }
}