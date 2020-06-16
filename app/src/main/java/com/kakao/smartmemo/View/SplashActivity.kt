package com.kakao.smartmemo.View

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast


class SplashActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        checkInternetState()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun checkInternetState() {
        var connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetwork != null) {
            //Toast.makeText(applicationContext, "네트워크 연결 되어있음.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "네트워크 연결이 안되어있습니다. 네트워크를 연결해주십시오.", Toast.LENGTH_SHORT).show()
        }
    }
}