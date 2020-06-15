package com.kakao.smartmemo.Receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import com.kakao.smartmemo.View.AddTodo
import com.kakao.smartmemo.View.MainActivity
import java.util.*

class PlaceReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val todoTitle = intent?.getStringExtra("todoTitle")
        val todoPlace = intent?.getStringExtra("todoPlace")
        val id = intent?.getIntExtra("todoId", 0) as Int
        val todoText = intent?.getStringExtra("todoText")
        Log.v("seyuuuun", "(id) in place : " + id.toString())

        val icon_noti = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.bell_icon_on)

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val cancelIntent = Intent(context, AddTodo::class.java)
        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        cancelIntent.putExtra(BROADCASTPLACE, true)
        cancelIntent.putExtra("placeid", id)

        val pendingIntent = PendingIntent.getActivity(context, id, notificationIntent, 0)
        val cancelpendingIntent = PendingIntent.getActivity(context, id, cancelIntent, 0)

        //헤드업알림
        val contentview = RemoteViews(context.packageName, R.layout.location_notification)
        contentview.setTextViewText(R.id.notification_Title, "TODOLIST 장소알림") //title
        contentview.setTextViewText(R.id.textView_alarm, todoTitle)  //content
        contentview.setTextViewText(R.id.textView_alarmlocation, todoPlace)
        contentview.setOnClickPendingIntent(R.id.cancel_notification, cancelpendingIntent)

        val notificationbuilder  = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(icon_noti)
            .setContentIntent(pendingIntent) // 알림을 눌렀을때 실행할 작업 인텐트 설정
            .setWhen(System.currentTimeMillis()) //miliSecond단위로 넣어주면 내부적으로 파싱함.
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setFullScreenIntent(pendingIntent,true) //헤드업알림
            .setContent(contentview)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Oreo 버전 이후부터 channel설정해줘야함.
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = CHANNEL_DESCRITION }

            notificationManager.createNotificationChannel(serviceChannel)
        }

        notificationManager?.notify(id, notificationbuilder.build())
        Log.v("seyuuuun", "notificationplaceID in broadcast " + id.toString())
    }

    companion object {
        val CHANNEL_ID = "장소알림"
        val CHANNEL_NAME = "장소알림채널"
        val CHANNEL_DESCRITION = "장소알림채널 리시버"
        private const val PACKAGE_NAME = "com.kakao.smartmemo"
        val BROADCASTPLACE = "$PACKAGE_NAME.broadcastplace"
    }
}