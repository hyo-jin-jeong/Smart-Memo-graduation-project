package com.kakao.smartmemo.Receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class DeviceBootAlarmReceiver : BroadcastReceiver() { //재부팅후에도 알림이 동작하도록
    // 부팅이 끝나면 alarm reset
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        val manager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val current_cal = Calendar.getInstance()

        if(manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, current_cal.timeInMillis , AlarmManager.INTERVAL_DAY, pendingIntent)
            //여러번 알람, 24*60*60*1000 하루에 한번 계속 알람.
            // manager.setRepeating(AlarmManager.RTC, current_cal.timeInMillis, pendingIntent) -> 한번 알람.
        }
   }
}