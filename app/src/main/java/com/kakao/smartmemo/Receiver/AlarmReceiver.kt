package com.kakao.smartmemo.Receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.kakao.smartmemo.R
import com.kakao.smartmemo.View.MainActivity
import com.kakao.smartmemo.View.TodoListActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
            val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val style = NotificationCompat.BigTextStyle()
            val pendingIntent = PendingIntent.getActivity(context, 2, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val LargeIconForNoti = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.todolist_icon)

            val notificationbuilder  = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(LargeIconForNoti)
                .setContentIntent(pendingIntent) // 알림을 눌렀을때 실행할 작업 인텐트 설정
                .setWhen(System.currentTimeMillis()) //miliSecond단위로 넣어주면 내부적으로 파싱함.
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                //.setStyle(style)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setFullScreenIntent(pendingIntent,true) //헤드업알림
                .setNumber(999) //확인하지않은 알림 개수 설정

            //헤드업알림
            val contentview = RemoteViews(context.packageName, R.layout.location_notification)
            contentview.setTextViewText(R.id.notification_Title, "장 소 알 람")
            contentview.setOnClickPendingIntent(R.id.later_notification, pendingIntent)
            contentview.setOnClickPendingIntent(R.id.cancel_notification, pendingIntent)
            notificationbuilder.setContent(contentview)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Oreo 버전 이후부터 channel설정해줘야함.
                val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = CHANNEL_DESCRITION }

                notificationManager.createNotificationChannel(serviceChannel)
            }

            notificationManager?.notify(0, notificationbuilder.build())
        Log.v("seyuuuun", "Receivcer동작!!!! in AlarmReceiver")
    }

    companion object {
        val CHANNEL_ID = "장소알람"
        val CHANNEL_NAME = "알림채널 장소알람"
        val CHANNEL_DESCRITION = "알림채널 장소알람 리시버"
    }
}