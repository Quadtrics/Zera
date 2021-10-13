package com.example.zeraapp.activitys.activity

import android.R.string
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zeraapp.R
import com.example.zeraapp.utlis.Common
import com.example.zeraapp.utlis.getEtText
import com.example.zeraapp.utlis.setLog


class HowMuchActivity : AppCompatActivity() {
    lateinit var backHMC: ImageView
    lateinit var tv_calculate: TextView
    lateinit var et_pDepositAmount: EditText
    lateinit var rg_termPeriod: RadioGroup
    lateinit var spin_count: Spinner
    var termSelected: String = ""
    var countSelected: String = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_much)
        findId()
    }



    private fun findId() {
        backHMC = findViewById(R.id.backHMC)
        tv_calculate = findViewById(R.id.tv_calculate)
        et_pDepositAmount = findViewById(R.id.et_pDepositAmount)
        rg_termPeriod = findViewById(R.id.rg_termPeriod)
        spin_count = findViewById(R.id.spin_count)
        clicks()
        setSpinnerData()
    }

    private fun setSpinnerData() {
        var countValue: MutableList<String> = ArrayList()
        for (i in 1..30) {
            countValue.add(i.toString())
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countValue)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin_count.adapter = arrayAdapter
        spin_count.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val tutorialsName: String = parent.getItemAtPosition(position).toString()
                countSelected=tutorialsName
                setLog("MyValuesSelected"," "+tutorialsName)
//                Toast.makeText(parent.context, "Selected: $tutorialsName", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }

    private fun clicks() {
        backHMC.setOnClickListener {
            onBackPressed()
        }
        rg_termPeriod.setOnCheckedChangeListener({ group, checkedId ->

            var selectedId = rg_termPeriod.checkedRadioButtonId
            when (selectedId) {
                R.id.rb_year -> {
                    termSelected = "year"

                }
                R.id.rb_month -> {
                    termSelected = "month"

                }
                else -> termSelected = ""
            }

        })
        tv_calculate.setOnClickListener {
            if (getEtText(et_pDepositAmount).isEmpty() || getEtText(et_pDepositAmount).equals("")) {
                Common.showErrorFullMsg(
                    this@HowMuchActivity,
                    resources.getString(R.string.validation_money)
                )
            } else {
                val amtWithDraw: Double? = getEtText(et_pDepositAmount).toDouble()
                if (amtWithDraw != null) {
                    if (amtWithDraw == 0.0) {
                        Common.showErrorFullMsg(
                            this@HowMuchActivity,
                            resources.getString(R.string.validation_zero_amount)
                        )
                    } else if (termSelected.isEmpty() || termSelected.equals("")) {
                        Common.showErrorFullMsg(
                            this@HowMuchActivity,
                            resources.getString(R.string.validation_termPeriod)
                        )
                    } else {
                        var intent = Intent(this@HowMuchActivity, EarningWithActivity::class.java)
                        intent.putExtra("principalAmount",getEtText(et_pDepositAmount))
                        intent.putExtra("term",termSelected)
                        intent.putExtra("termCount",countSelected)
                        startActivity(intent)                    }
                }
            }
        }

    }
}