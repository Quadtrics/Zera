package com.example.zeraapp.activitys.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.zeraapp.R
import com.example.zeraapp.apis.ApiClient
import com.example.zeraapp.apis.ApiInterface
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import com.example.zeraapp.utlis.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DepositAmmountActivity : AppCompatActivity() {
    lateinit var tv_card1: TextView
    lateinit var tv_card2: TextView
    lateinit var tv_card3: TextView
    lateinit var tv_card4: TextView
    lateinit var tv_card5: TextView
    lateinit var tv_maturityDate: TextView
    lateinit var tv_nameHeader: TextView
    lateinit var cv_card1: CardView
    lateinit var cv_card2: CardView
    lateinit var cv_card3: CardView
    lateinit var cv_card4: CardView
    lateinit var cv_card5: CardView
    lateinit var rg_termPeriod: RadioGroup
    lateinit var tv_principalBalance: TextView
    lateinit var tv_timePeriod: TextView
    lateinit var tv_submitNext: TextView
    lateinit var et_depositAmount: EditText
    lateinit var sp_termCount: Spinner


    var amountDeposit: String = ""
    var termSelected: String = ""
    var timePeriodSelected: String = ""
    var dateOfMaturity: String = ""
    var apiService: ApiInterface? = null


    lateinit var tv_spinVal: TextView
    lateinit var rl_showCalculation: RelativeLayout
    lateinit var howWork: TextView
    lateinit var nextClick: RelativeLayout
    lateinit var rlDone: RelativeLayout
    lateinit var rlSteps: RelativeLayout
    lateinit var llBottomBtm: LinearLayout
    lateinit var rlAllDone: RelativeLayout
    private lateinit var viewPager: ViewPager
    private var layouts: IntArray? = null
    private var myViewPagerAdapter: MyViewPagerAdapterInfo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_ammount)
        findId()
    }

    private fun findId() {
        tv_spinVal = findViewById(R.id.tv_spinVal)
        tv_card1 = findViewById(R.id.tv_card1)
        cv_card1 = findViewById(R.id.cv_card1)
        tv_card2 = findViewById(R.id.tv_card2)
        cv_card2 = findViewById(R.id.cv_card2)
        tv_card3 = findViewById(R.id.tv_card3)
        cv_card3 = findViewById(R.id.cv_card3)
        tv_card4 = findViewById(R.id.tv_card4)
        cv_card4 = findViewById(R.id.cv_card4)
        tv_card5 = findViewById(R.id.tv_card5)
        cv_card5 = findViewById(R.id.cv_card5)
        tv_submitNext = findViewById(R.id.tv_submitNext)
        tv_nameHeader = findViewById(R.id.tv_nameHeader)


        sp_termCount = findViewById(R.id.sp_termCount)
        rg_termPeriod = findViewById(R.id.rg_termPeriod)
        et_depositAmount = findViewById(R.id.et_depositAmount)
        tv_principalBalance = findViewById(R.id.tv_principalBalance)
        tv_timePeriod = findViewById(R.id.tv_timePeriod)
        tv_maturityDate = findViewById(R.id.tv_maturityDate)
        rl_showCalculation = findViewById(R.id.rl_showCalculation)
        viewPager = findViewById(R.id.viewPager)
        nextClick = findViewById(R.id.nextClick)
        rlDone = findViewById(R.id.rlDone)
        howWork = findViewById(R.id.howWork)
        rlSteps = findViewById(R.id.rlSteps)
        llBottomBtm = findViewById(R.id.llBottomBtm)
        rlAllDone = findViewById(R.id.rlAllDone)
        layouts = intArrayOf(R.layout.step1, R.layout.step2, R.layout.step3, R.layout.step4)

        myViewPagerAdapter = MyViewPagerAdapterInfo()
        viewPager.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        clicks()
        setData()
    }

    private fun setSpinnerData() {
        var countValue: MutableList<String> = ArrayList()
//        countValue.add("Custom")
        if (termSelected == "year") {
            for (i in 1..8) {
                countValue.add(i.toString())
            }
        } else if (termSelected == "month") {
            for (i in 6..30) {
                countValue.add(i.toString())
            }
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countValue)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_termCount.adapter = arrayAdapter
        sp_termCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val tutorialsName: String = parent.getItemAtPosition(position).toString()
                setLog("MyValuesSelected", " " + tutorialsName + " " + position)
                tv_spinVal.text = tutorialsName
                selectBox(0)
                calculateMaturityDate(Integer.parseInt(tutorialsName), termSelected)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun setData() {
        tv_card1.text = "6 \n Months"
        tv_card2.text = "12 \n Months"
        tv_card3.text = "18 \n Months"
        tv_card4.text = "24 \n Months"
        tv_card5.text = "30 \n Months"
        tv_nameHeader.text =
            resources.getString(R.string.hello)+", " + extractWord(
                SharePreference.getStringPref(
                    this@DepositAmmountActivity,
                    SharePreference.userFirstName).toString()
            )


    }


    private fun clicks() {
        tv_spinVal.setOnClickListener {
            sp_termCount.performClick()
        }

        cv_card1.setOnClickListener {
            selectBox(1)
        }
        cv_card2.setOnClickListener {
            selectBox(2)
        }
        cv_card3.setOnClickListener {
            selectBox(3)
        }
        cv_card4.setOnClickListener {
            selectBox(4)
        }
        cv_card5.setOnClickListener {
            selectBox(5)
        }

        rl_showCalculation.setOnClickListener {
            var intent = Intent(this@DepositAmmountActivity, HowMuchActivity::class.java)
            startActivity(intent)
        }
        tv_submitNext.setOnClickListener {
            if (amountDeposit.isEmpty() || amountDeposit.equals("")) {
                Common.showErrorFullMsg(
                    this@DepositAmmountActivity,
                    resources.getString(R.string.validation_money)
                )
            } else if (timePeriodSelected.isEmpty() || timePeriodSelected.equals("")) {
                Common.showErrorFullMsg(
                    this@DepositAmmountActivity,
                    resources.getString(R.string.validation_termPeriod)
                )
            } else if (dateOfMaturity.isEmpty() || dateOfMaturity.equals("")) {
                Common.showErrorFullMsg(
                    this@DepositAmmountActivity,
                    resources.getString(R.string.validation_termPeriod)
                )
            } else {
                val amtWithDraw: Double? = amountDeposit.toDouble()
                if (amtWithDraw != null) {
                    if (amtWithDraw < 500.0) {
                        Common.showErrorFullMsg(
                            this@DepositAmmountActivity,
                            resources.getString(R.string.minimumAmount500)
                        )
                    } else {
                        updateMaturity()
                    }
                }


            }
        }
        et_depositAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                amountDeposit = getEtText(et_depositAmount)
                tv_principalBalance.text = makeDollars(amountDeposit)

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        howWork.setOnClickListener {
            llBottomBtm.visibility = View.VISIBLE
            rlAllDone.visibility = View.GONE
            myViewPagerAdapter = MyViewPagerAdapterInfo()
            viewPager.adapter = myViewPagerAdapter
            viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
            rlSteps.visibility = View.VISIBLE
        }

        nextClick.setOnClickListener({
            // checking for last page if true launch MainActivity
            val current: Int = getItem(+1)
            if (current == 3) {
                llBottomBtm.visibility = View.GONE
                rlAllDone.visibility = View.VISIBLE
            }
            if (current < layouts!!.size) {
                // move to next screen
                viewPager.currentItem = current
            } else {
//                rlSteps.visibility = View.GONE
            }
        })
        rlDone.setOnClickListener(View.OnClickListener {
            rlSteps.visibility = View.GONE
        })
        rlAllDone.setOnClickListener(View.OnClickListener {
            rlSteps.visibility = View.GONE

        })
        rg_termPeriod.setOnCheckedChangeListener { group, checkedId ->
            var selectedId = rg_termPeriod.checkedRadioButtonId
            when (selectedId) {
                R.id.rb_year -> {
                    termSelected = "year"
                    setSpinnerData()
                }
                R.id.rb_month -> {
                    termSelected = "month"
                    setSpinnerData()
                }
            }

        }

    }

    private fun selectBox(cardPosition: Int) {
        cv_card1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        cv_card2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        cv_card3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        cv_card4.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        cv_card5.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        if (cardPosition != 0) {
            termSelected = "month"
            tv_spinVal.setText("Custom")

        }
        if (cardPosition == 1) {
            cv_card1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            calculateMaturityDate(6, termSelected)
        } else if (cardPosition == 2) {
            cv_card2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            calculateMaturityDate(12, termSelected)
        } else if (cardPosition == 3) {
            cv_card3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            calculateMaturityDate(18, termSelected)
        } else if (cardPosition == 4) {
            cv_card4.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            calculateMaturityDate(24, termSelected)
        } else if (cardPosition == 5) {
            cv_card5.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            calculateMaturityDate(30, termSelected)
        } else {
            calculateMaturityDate(0, termSelected)
        }

    }

    private fun calculateMaturityDate(count: Int, type: String) {
        var months = count
        if (count == 0) {
            timePeriodSelected = ""
            dateOfMaturity = ""
            tv_maturityDate.text = "MM/dd/yyyy"
        } else if (type.equals("year")) {
            months = count * 12
            timePeriodSelected = count.toString() + "-" + type

        } else {
            months = count
            timePeriodSelected = months.toString() + "-" + type

        }
        dateOfMaturity = addMonths(months, "yyyy-MM-dd")
        tv_maturityDate.text = addMonths(months, "MM/dd/yyyy")
        tv_timePeriod.text = timePeriodSelected
    }


    inner class MyViewPagerAdapterInfo : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            }

            override fun onPageScrollStateChanged(arg0: Int) {
            }
        }

    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }

    private fun updateMaturity() {
        Log.e("updateMaturity", "START")
        Common.showLoadingProgress(this@DepositAmmountActivity)
        apiService = ApiClient.getClientToken(
            SharePreference.getStringPref(
                this@DepositAmmountActivity,
                SharePreference.loginToken
            ).toString()
        )!!.create(ApiInterface::class.java)
        apiService!!.userUpdate(
            SharePreference.getStringPref(this@DepositAmmountActivity, SharePreference.userId)
                .toString(),
            timePeriodSelected.lowercase(Locale.getDefault()),
            dateOfMaturity

        )!!.enqueue(object :
            Callback<SignupPojo?> {
            override fun onResponse(
                call: Call<SignupPojo?>,
                response: Response<SignupPojo?>
            ) {
                when {
                    response.code() == 200 -> {
                        Common.dismissLoadingProgress()
                        val loginList: SignupPojo? = response.body()
                        SharePreference.setStringPref(
                            this@DepositAmmountActivity,
                            SharePreference.maturity_date,
                            loginList!!.data!!.maturity_date.toString()
                        )
                        var intent =
                            Intent(this@DepositAmmountActivity, DocumentSignature::class.java)
                        intent.putExtra("depositAmount", getEtText(et_depositAmount))
                        intent.putExtra("timePeriodSelected", timePeriodSelected)
                        intent.putExtra("dateOfMaturity", dateOfMaturity)
                        startActivity(intent)
                        Log.e("updateMaturity", "SUCCESS")

                    }
                    response.code() == 401 -> {
                        Common.dismissLoadingProgress()
                        Log.e("updateMaturity", "401")

                        Toast.makeText(
                            this@DepositAmmountActivity,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        val intent = Intent(this@DepositAmmountActivity, MainActivity::class.java)
                        startActivity(intent)

                    }
                    response.code() == 400 -> {
                        Common.dismissLoadingProgress()
                        Log.e("updateMaturity", "400")
                        Toast.makeText(
                            this@DepositAmmountActivity,
                            "Invalid Credentials",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return
                    }
                    else -> {
                        Common.dismissLoadingProgress()
                        Log.e("updateMaturity", response.code().toString())
                        Toast.makeText(
                            this@DepositAmmountActivity,
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
                Log.e("updateMaturity", "onFailure")
                Log.d("TAG", "onFailure: " + t.message)
            }


        })
    }


}