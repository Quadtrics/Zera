package com.example.zeraapp.activitys.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.zeraapp.R
import com.example.zeraapp.fragments.HomeFragment
import com.example.zeraapp.fragments.ProfileFragment
import com.example.zeraapp.fragments.TransactionFragment
import com.example.zeraapp.fragments.WalletFragment

class DashBoardActivity : AppCompatActivity() {
    lateinit var imgHome:ImageView
    lateinit var imgWallet:ImageView
    lateinit var imgTrangaction:ImageView
    lateinit var imgProfile:ImageView
    lateinit var rlHome:RelativeLayout
    lateinit var rlWallet:RelativeLayout
    lateinit var rlTransaction:RelativeLayout
    lateinit var rlProfile:RelativeLayout
    lateinit var next:String
    lateinit var next2:String
    lateinit var next3:String
    lateinit var next4:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
        findId()


    }

    private fun findId() {
        rlHome = findViewById(R.id.rlHome)
        rlWallet = findViewById(R.id.rlWallet)
        rlTransaction = findViewById(R.id.rlTransaction)
        rlProfile = findViewById(R.id.rlProfile)
        imgHome = findViewById(R.id.imgHome)
        imgWallet = findViewById(R.id.imgWallet)
        imgTrangaction = findViewById(R.id.imgTrangaction)
        imgProfile = findViewById(R.id.imgProfile)

        var intent = intent
        next = intent.getStringExtra("nextTrans").toString()
        if (next.equals("1")){
            replaceFragment(TransactionFragment())
            imgHome.setImageResource(R.drawable.ic_footer_dahsboard_svg)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_svg)
            imgTrangaction.setImageResource(R.drawable.ic_list_footer_seletd_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_combined_svg)
        }


        var intent2 = intent
        next2 = intent2.getStringExtra("nextWothdraw").toString()
        if (next2.equals("1")){
            replaceFragment(WalletFragment())
            imgHome.setImageResource(R.drawable.ic_footer_dahsboard_svg)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_selected_svg)
            imgTrangaction.setImageResource(R.drawable.ic_footer_report_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_combined_svg)
        }

       var intent3 = intent
        next3 = intent3.getStringExtra("nextRedeposit").toString()
        if (next3.equals("1")){
            replaceFragment(WalletFragment())
            imgHome.setImageResource(R.drawable.ic_footer_dahsboard_svg)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_selected_svg)
            imgTrangaction.setImageResource(R.drawable.ic_footer_report_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_combined_svg)
        }
       var intent4 = intent
        next4 = intent4.getStringExtra("profile").toString()
        if (next4.equals("1")){
            replaceFragment(ProfileFragment())
            imgHome.setImageResource(R.drawable.ic_footer_dahsboard_svg)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_svg)
            imgTrangaction.setImageResource(R.drawable.ic_footer_report_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_selected_svg)
        }

        clicks()
    }

    private fun clicks() {
        rlHome.setOnClickListener {
            replaceFragment(HomeFragment())
            imgHome.setImageResource(R.drawable.home_select)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_svg)
            imgTrangaction.setImageResource(R.drawable.ic_footer_report_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_combined_svg)
        }
        rlWallet.setOnClickListener {
            replaceFragment(WalletFragment())
            imgHome.setImageResource(R.drawable.ic_footer_dahsboard_svg)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_selected_svg)
            imgTrangaction.setImageResource(R.drawable.ic_footer_report_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_combined_svg)
        }
        rlTransaction.setOnClickListener {
            replaceFragment(TransactionFragment())
            imgHome.setImageResource(R.drawable.ic_footer_dahsboard_svg)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_svg)
            imgTrangaction.setImageResource(R.drawable.ic_list_footer_seletd_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_combined_svg)
        }
        rlProfile.setOnClickListener {
            replaceFragment(ProfileFragment())
            imgHome.setImageResource(R.drawable.ic_footer_dahsboard_svg)
            imgWallet.setImageResource(R.drawable.ic_footer_wallet_svg)
            imgTrangaction.setImageResource(R.drawable.ic_footer_report_svg)
            imgProfile.setImageResource(R.drawable.ic_footer_selected_svg)
        }
    }

    @SuppressLint("WrongConstant")
    fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}