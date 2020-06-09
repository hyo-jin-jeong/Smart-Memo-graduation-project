package com.kakao.smartmemo.Receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.kakao.smartmemo.R
import com.kakao.smartmemo.View.AddTodo
import com.kakao.smartmemo.View.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val repeatIntent = Intent(context, AddTodo::class.java)
        repeatIntent.putExtra(BROADCAST, 5)

        val cancelIntent = Intent(context, AddTodo::class.java)
        cancelIntent.putExtra(BROADCAST2, true)

        val pendingIntent = PendingIntent.getActivity(context, 2, notificationIntent, 0)
        val repeatpendingIntent = PendingIntent.getActivity(context, 3, repeatIntent, 0)
        val cancelpendingIntent = PendingIntent.getActivity(context, 4, cancelIntent, 0)

        //헤드업알림
        val contentview = RemoteViews(context.packageName, R.layout.location_notification)
        contentview.setTextViewText(R.id.notification_Title, "알 람")
        contentview.setOnClickPendingIntent(R.id.later_notification, repeatpendingIntent)
        contentview.setOnClickPendingIntent(R.id.cancel_notification, cancelpendingIntent)

        val notificationbuilder  = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent) // 알림을 눌렀을때 실행할 작업 인텐트 설정
            .setWhen(System.currentTimeMillis()) //miliSecond단위로 넣어주면 내부적으로 파싱함.
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            //.addAction(R.drawable.bell_icon, "다시 알림", repeatpendingIntent)
            //.addAction(R.drawable.delete_icon, "알림 해제", cancelpendingIntent)
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

        notificationManager?.notify(NOTIFICATIONID, notificationbuilder.build())
        Log.v("seyuuuun", "notificationID in broadcast" + NOTIFICATIONID.toString())
    }

    companion object {
        val CHANNEL_ID = "알람"
        val CHANNEL_NAME = "알림채널"
        val CHANNEL_DESCRITION = "알림채널 리시버"
        private const val PACKAGE_NAME = "com.kakao.smartmemo"
        val BROADCAST = "$PACKAGE_NAME.broadcast"
        val BROADCAST2 = "$PACKAGE_NAME.broadcast2"
        val NOTIFICATION_NAME = "NotificationID"
        val NOTIFICATIONID = (System.currentTimeMillis()/1000).toInt()
    }
}