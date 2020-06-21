package com.kakao.smartmemo.Receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class DeviceBootPlaceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val placeIntent = Intent(context, PlaceReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, 2, placeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val currentCal = Calendar.getInstance()

        manager.setRepeating(AlarmManager.RTC_WAKEUP, currentCal.timeInMillis , AlarmManager.INTERVAL_DAY, pendingIntent)
    }
}