package com.example.zeraapp.activitys.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.zeraapp.R
import com.example.zeraapp.utlis.SharePreference
import com.example.zeraapp.utlis.SharePreference.Companion.appLanguage
import com.example.zeraapp.utlis.SharePreference.Companion.getStringPref
import com.example.zeraapp.utlis.setLocale
import com.example.zeraapp.utlis.setLog
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var lastLang: String = getStringPref(this@SplashActivity,appLanguage).toString()
        setLog("MyLanguage",lastLang+" ")

        if(!lastLang.isEmpty()) {
            setLocale(this@SplashActivity, lastLang,false)
        }else{
            SharePreference.setStringPref(this,SharePreference.appLanguage,"en")
            setLocale(this@SplashActivity, "en",false)
        }

        if (permission) {
            moveToActivity()
        }
    }

    companion object {
        private const val permission_granted_code = 100
        private val appPermission = arrayOf(
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        )

        fun showAlertDialog(
            context: Context?, title: String?, message: String?, positiveLabel: String?,
            positiveClick: DialogInterface.OnClickListener?, negativeLabel: String?,
            negativeClick: DialogInterface.OnClickListener?, isCancelable: Boolean
        ): AlertDialog? {
            try {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setTitle(title)
                dialogBuilder.setCancelable(isCancelable)
                dialogBuilder.setMessage(message)
                dialogBuilder.setPositiveButton(positiveLabel, positiveClick)
                dialogBuilder.setNegativeButton(negativeLabel, negativeClick)
                val alertDialog = dialogBuilder.create()
                alertDialog.show()
                return alertDialog
            } catch (e: Exception) {
            }
            return null
        }
    }

    private fun moveToActivity() {
/*
        startActivity(Intent(this, DocumentSignature::class.java))
        finish()
*/
        Handler(Looper.getMainLooper()).postDelayed({

            if (!getStringPref(this@SplashActivity, SharePreference.loginToken).equals("")) {
                val dateMaturity = getStringPref(this@SplashActivity, SharePreference.maturity_date)
                val isFirstDepositAmount = getStringPref(this@SplashActivity, SharePreference.isFirstDeposit)
                val contract = getStringPref(this@SplashActivity, SharePreference.contract)

                if (dateMaturity != null) {
                    if (contract.equals("null")) {
                        val intent =
                            Intent(this@SplashActivity, DepositAmmountActivity::class.java)
                        startActivity(intent)
                    } else if (isFirstDepositAmount.equals("0")||isFirstDepositAmount.equals("")) {
                        val intent =
                            Intent(this@SplashActivity, DepositAmmountActivity::class.java)
                        startActivity(intent)
                    } else if (dateMaturity.equals("0") || dateMaturity.isEmpty()) {
                        val intent =
                            Intent(this@SplashActivity, DepositAmmountActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    val intent =
                        Intent(this@SplashActivity, DepositAmmountActivity::class.java)
                    startActivity(intent)
                }
                finish()
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }

    val permission: Boolean
        get() {
            val listPermissionsNeedeFor: MutableList<String> = ArrayList()
            for (permission in appPermission) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    listPermissionsNeedeFor.add(permission)
                }
            }
            if (!listPermissionsNeedeFor.isEmpty()) {
                ActivityCompat.requestPermissions(
                    this,
                    listPermissionsNeedeFor.toTypedArray(),
                    permission_granted_code
                )
                return false
            }
            return true
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permission_granted_code) {
            val permisionResults = HashMap<String, Int>()
            var deniedPermissionCount = 0
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permisionResults[permissions[i]] = grantResults[i]
                    deniedPermissionCount++
                }
            }
            if (deniedPermissionCount == 0) {
                moveToActivity()
            } else {
                for ((permName, permResult) in permisionResults) {
                    Log.v("MyPermissions", "denied  $permName")
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        showAlertDialog(
                            this@SplashActivity,
                            "",
                            "Do allow or permission to make application work fine",
                            "Yes, Grant Permission",
                            { dialogInterface: DialogInterface, i: Int ->
                                dialogInterface.dismiss()
                                permission
                            },
                            "No, Exit app",
                            { dialogInterface: DialogInterface, i: Int ->
                                dialogInterface.dismiss()
                                finish()
                            },
                            false
                        )
                    } else {
                        showAlertDialog(
                            this@SplashActivity,
                            "",
                            "You have denied some permissons. Allow all permissions at [Settings] > [Permissions]",
                            "Go to settings",
                            { dialogInterface: DialogInterface, i: Int ->
                                dialogInterface.dismiss()
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            },
                            "No, Exit app",
                            { dialogInterface: DialogInterface, i: Int ->
                                dialogInterface.dismiss()
                                finish()
                            },
                            false
                        )
                        break
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


}