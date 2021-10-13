package com.example.zeraapp.activitys.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.zeraapp.R
import com.example.zeraapp.utlis.SharePreference

class
MainActivity : AppCompatActivity() {

    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private var layouts: IntArray? = null
    lateinit var signUp: TextView
    lateinit var login: TextView
    private lateinit var view_pager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clearData()

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        dotsLayout = findViewById(R.id.layoutDots)
        view_pager = findViewById(R.id.view_pager)
        signUp = findViewById(R.id.signUp)
        login = findViewById(R.id.login)
        layouts = intArrayOf(R.layout.slide1, R.layout.slide2, R.layout.slide3)

        addBottomDots(0)

        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()
        view_pager.adapter = myViewPagerAdapter
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener)

        signUp.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun clearData() {
        SharePreference.setStringPref(this@MainActivity, SharePreference.userId, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userFirstName, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userLastName, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userEmail, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userPassword, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userNetBalance, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userNetEarn, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userNetInterest, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userMobile, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.incomingBalance, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.principalBalance, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.bankStatus, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.bankVerifyStatus, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.clientId, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.isLogin, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.loginToken, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userProfileImage, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.maturity_date, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.userName, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.isFirstDeposit, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.contract, "")
        SharePreference.setStringPref(this@MainActivity, SharePreference.isRenew, "")

    }

    private fun addBottomDots(currentPage: Int) {
        var dots: Array<TextView?> = arrayOfNulls(layouts!!.size)

        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(resources.getIntArray(R.array.array_dot_inactive)[currentPage])
            dotsLayout!!.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentPage]!!.setTextColor(resources.getColor(R.color.yellow))
    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                addBottomDots(position)
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            }

            override fun onPageScrollStateChanged(arg0: Int) {
            }
        }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
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

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}