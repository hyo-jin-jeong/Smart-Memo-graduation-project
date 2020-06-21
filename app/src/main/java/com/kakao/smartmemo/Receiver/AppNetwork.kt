package com.kakao.smartmemo.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class AppNetwork : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
            try {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetInfo = connectivityManager.activeNetwork
                if (activeNetInfo == null) {
                    Toast.makeText(context, "Network disconnected\nPlease connect WI-FI or Cellular", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.i("ULNetworkReceiver", e.message)
            }
        }

    }
}
