package com.example.zeraapp.utlis

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharePreference {

    companion object {
        lateinit var mContext: Context
        lateinit var sharedPreferences: SharedPreferences
        val PREF_NAME: String = "FoodApp"
        val PRIVATE_MODE: Int = 0
        lateinit var editor: SharedPreferences.Editor

        var appLanguage:String="appLanguage"
        var userId:String="userid"
        var userFirstName:String="userfirstname"
        var userLastName:String="userlastname"
        var userEmail:String="useremail"
        var userPassword:String="userPassword"
        var userNetBalance:String="userNetBalance"
        var userNetEarn:String="userNetEarn"
        var userNetInterest:String="userNetInterest"
        var userMobile:String="userMobile"
        var incomingBalance:String="incomingBalance"
        var principalBalance:String="principalBalance"
        var bankStatus:String="bankStatus"
        var bankVerifyStatus:String="bankVerifyStatus"
        var clientId:String="clientId"
        var userName:String="userName"
        var userProfileImage:String="userProfileImage"
        var maturity_date:String="maturity_date"
        var isFirstDeposit:String="isFirstDeposit"
        var contract:String="contract"
        var isRenew:String="isRenew"

        val isLogin:String="isLogin"
        val loginToken:String="logintoken"
        val item_pricing_id:String="item_pricing_id"
        val user_name:String="size_name"
        val storeId:String="storeId"
        val orderIds = "orderIds"
        val newOderID:String="newOderID"
        val empCode:String="empCode"
        val userProfile:String="userprofile"
        val isTutorial="tutorial"
        val get_order_id="get_order_id"
        val isFavourite="favourite"
        val isCurrancy="Currancy"
        val isMiniMum="MiniMum"
        val isMaximum="Maximum"
        val isMiniMumQty="isMiniMumQty"
        val isLinearLayoutManager="Grid"
        val isQWERTY="QWERTY"
        val buzzer = "buzzer"
        val check = "check"
        var SELECTED_LANGUAGE = "selected_language"
        fun getIntPref(context: Context,key:String):Int{
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            return pref.getInt(key,-1)
        }
        fun setIntPref(context: Context,key: String,value:Int){
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            pref.edit().putInt(key,value).apply()
        }

        fun getStringPref(context: Context,key:String): String? {
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            return pref.getString(key,"")
        }

        fun setStringPref(context: Context, key: String, value: String?){
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            pref.edit().putString(key,value).apply()
        }

        fun getBooleanPref(context: Context,key:String): Boolean {
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            return pref.getBoolean(key,false)
        }

        fun setBooleanPref(context: Context,key: String,value:Boolean){
            val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            pref.edit().putBoolean(key,value).apply()
        }

    }

    @SuppressLint("CommitPrefEdits")
    constructor(mContext1: Context) {
        mContext = mContext1
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor= sharedPreferences.edit()
    }
    fun mLogout(){
        editor.clear()
        editor.commit()
    }

}