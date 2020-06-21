package com.kakao.smartmemo.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class SplashActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}