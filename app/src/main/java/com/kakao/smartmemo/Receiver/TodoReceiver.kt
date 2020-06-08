package com.kakao.smartmemo.Receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.kakao.smartmemo.R
import com.kakao.smartmemo.View.AddTodo
import com.kakao.smartmemo.View.MainActivity

class TodoReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationbuilder  = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent) // 알림을 눌렀을때 실행할 작업 인텐트 설정
            .setWhen(System.currentTimeMillis()) //miliSecond단위로 넣어주면 내부적으로 파싱함.
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setFullScreenIntent(pendingIntent,true) //헤드업알림

        //헤드업알림
        val contentview = RemoteViews(context.packageName, R.layout.todolist_headup_alarm)
        contentview.setTextViewText(R.id.notification_Title, "Todolist")
        contentview.setTextViewText(R.id.todolist_textView, "1. 영양제 먹기\n2. 학교 가기")
        contentview.setTextViewText(R.id.todolist_location, "● 삼성약국/녹십자약국/온누리약국")
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

        notificationManager?.notify(9999, notificationbuilder.build())
    }

    companion object {
        val CHANNEL_ID = "TODO"
        val CHANNEL_NAME = "알림채널 TODO"
        val CHANNEL_DESCRITION = "알림채널 TODO 리시버"
    }
}