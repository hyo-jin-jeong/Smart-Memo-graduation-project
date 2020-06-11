package com.kakao.smartmemo.Service

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Receiver.DeviceBootPlaceReceiver
import com.kakao.smartmemo.Receiver.DeviceBootTimeReceiver
import com.kakao.smartmemo.Receiver.PlaceReceiver
import com.kakao.smartmemo.Receiver.TimeReceiver
import com.kakao.smartmemo.Utils.Utils.getLocationText
import com.kakao.smartmemo.Utils.Utils.getLocationTitle
import com.kakao.smartmemo.Utils.Utils.requestingLocationUpdates
import com.kakao.smartmemo.Utils.Utils.setRequestingLocationUpdates
import com.kakao.smartmemo.View.AddTodo
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class LocationUpdatesService : Service() {
    private val mBinder: IBinder = LocalBinder()

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var mChangingConfiguration = false
    private var mNotificationManager: NotificationManager? = null
    private var AlarmNotificationManager: NotificationManager? = null
    /**
     * Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
     */
    private var mLocationRequest: LocationRequest? = null

    private val PlaceNotificationID = (System.currentTimeMillis()/1000).toInt()
    private val placeCalendar = Calendar.getInstance()
    private var addTodo : AddTodo = AddTodo()
    private var settingsTime = 0

    /**
     * Provides access to the Fused Location Provider API.
     */
    //구글API에접근해 융합된 주위정보를 얻는 변수
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Callback for changes in location.
     */
    private var mLocationCallback: LocationCallback? = null
    private var mServiceHandler: Handler? = null

    /**
     * The current location.
     */
    private var mLocation: Location? = null

    override fun onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationCallback = object : LocationCallback() { //장치 위치가 변경되었거나 더이상 확인할수 없을때로부터 알림을받음.
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        getLastLocation()
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()  //create될때 백그라운드에서 thread가 돌도록함.
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        AlarmNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            // Create the channel for the notification
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager!!.createNotificationChannel(mChannel)
        }

    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int { //startService()를 호출해 서비스가 시작되면 메소드 호출
        Log.i(TAG, "Service started")
        val startedFromNotification = intent.getBooleanExtra(
            EXTRA_STARTED_FROM_NOTIFICATION,
            false
        )

        val extraValue = intent.getBooleanExtra(EXTRA_START_FROM_NOTIFICATION, false)
        if (extraValue) {
            AlarmNotificationManager!!.cancel(NOTIFICATION_ID_NOTIFICATION)
        }

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()  //스스로 서비스 중단
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY //서비스가 강제 종료된후 다시 시작안함.
        /*START_STICKY : 서비스가 강제로 종료됬을때, intent를 초기화하면서 다시 시작
        START_NOT_STICKY : 서비스가 강제로 종료된후다시 시작하지 않겠다
        START_REDELIVER_STICKY : 서비스가 강제로 종료됬을때, 기존의 intent를 유지*/
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onBind(intent: Intent): IBinder? { //onCreate다음에 호출
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()")
        stopForeground(true)  //상태 표시줄 알림 제거
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent) { //onUnbind일때
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()")
        stopForeground(true) //상태 표시줄 알림 제거
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean { //bindService종료 꼭 해줘야 제대로 종료됨!
        Log.i(TAG, "Last client unbound from service")

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service")
            startForeground(NOTIFICATION_ID, getNotification()) //서비스가 실행되는 동안에 상태 표시줄에 알림 표시
        }
        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {  //바인드 서비스 종료
        mServiceHandler!!.removeCallbacksAndMessages(null)
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        setRequestingLocationUpdates(this, true)
        startService(Intent(applicationContext, LocationUpdatesService::class.java))
        try {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            setRequestingLocationUpdates(this, false)
            Log.e(
                TAG,
                "Lost location permission. Could not request updates. $unlikely"
            )
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            setRequestingLocationUpdates(this, false)
            stopSelf()
        } catch (unlikely: SecurityException) {
            setRequestingLocationUpdates(this, true)
            Log.e(
                TAG,
                "Lost location permission. Could not remove updates. $unlikely"
            )
        }
    }

    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
    private fun getNotification(): Notification {
        val intent = Intent(this, LocationUpdatesService::class.java)
        val text: CharSequence = getLocationText(mLocation)
        val contentText = "앱의 장소 알람 기능을 사용하시려 알람을 유지해주세요.\\n이 알림을 끄면 장소 알림이 제대로 실행되지 않을 수 있습니다."

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        val servicePendingIntent = PendingIntent.getService(   //intent넘김
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // The PendingIntent to launch activity.
        val activityPendingIntent = PendingIntent.getActivity(  //AddTodo으로 넘어감
            this, 0,
            Intent(this, AddTodo::class.java), 0
        )

//        val builder = Notification.Builder(this)
//        val remoteViews = RemoteViews(packageName, R.layout.custom_notif)
//        builder.setCustomContentView(remoteViews)
//        //builder.setSmallIcon(R.mipmap.ic_launcher)
//        builder.setSmallIcon(R.mipmap.app_icon)
//        builder.setPriority(Notification.PRIORITY_LOW)
//        builder.setVisibility(Notification.VISIBILITY_SECRET)

        //val remoteViews = RemoteViews(packageName, R.layout.custom_notif)
        val builder =
            NotificationCompat.Builder(this)
                .setContentTitle("백그라운드에서 대기 중")
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                //.setCustomContentView(remoteViews)
                .setContentIntent(activityPendingIntent)
                .setContentText(contentText)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.app_icon)
                .setWhen(System.currentTimeMillis())

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID) // Channel ID
        }
        return builder.build()
    }

    private fun receiver() {
        val text: CharSequence = getLocationText(mLocation)

        val notificationIntent = Intent(this, LocationUpdatesService::class.java)
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        notificationIntent.putExtra(EXTRA_START_FROM_NOTIFICATION, true)

        val pendingIntent =
            PendingIntent.getService(this, 5, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationbuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .addAction(R.drawable.ic_cancel, "알림 해제", pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent) // 알림을 눌렀을때 실행할 작업 인텐트 설정
            .setWhen(System.currentTimeMillis()) //miliSecond단위로 넣어주면 내부적으로 파싱함.
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentTitle("장소 알람")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentText(text)
            .setTicker(text)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setFullScreenIntent(pendingIntent, true) //헤드업알림

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Oreo 버전 이후부터 channel설정해줘야함.
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = CHANNEL_DESCRITION }

            AlarmNotificationManager?.createNotificationChannel(serviceChannel)
        }

        AlarmNotificationManager?.notify(NOTIFICATION_ID_NOTIFICATION, notificationbuilder.build())
    }

    private fun setPlaceAlarm(calendar : Calendar) {
        val text: CharSequence = getLocationText(mLocation)

        val pm = this.packageManager
        val placereceiver = ComponentName(this, DeviceBootPlaceReceiver::class.java)
        val placealarmIntent = Intent(this, PlaceReceiver::class.java)

        placealarmIntent.putExtra("todoTitle", "약사러 가기")

        //DB작업끝난후 바꿔야함.
        placealarmIntent.putExtra("todoPlace", "온누리 약국")
        placealarmIntent.putExtra("todoText", text)

        placealarmIntent.putExtra("todoId", PlaceNotificationID) //reqeustcode 때문에 넣어준 것!!
        Log.v("seyuuuun", "notificationId in place" + PlaceNotificationID)

        val pendingIntent = PendingIntent.getBroadcast(this, PlaceNotificationID, placealarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val interval = 1000*60*3  //3분

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    interval.toLong(),
                    pendingIntent
                )
            }
            //부팅후 실행되는 리시버 사용가능하게 설정함.
            pm.setComponentEnabledSetting(
                placereceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    private fun unsetPlaceAlarm() {
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootTimeReceiver::class.java)
        val alarmIntent = Intent(this, TimeReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, PlaceNotificationID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(PendingIntent.getBroadcast(this, PlaceNotificationID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)!=null && alarmManager!=null) {
            alarmManager.cancel(pendingIntent)
            Log.v("seyuuuun", "알림해제 in place")
        }
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                    } else {
                        Log.w(
                            TAG,
                            "Failed to get location."
                        )
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(
                TAG,
                "Lost location permission.$unlikely"
            )
        }
    }

    private fun onNewLocation(location: Location) {
        Log.i(TAG, "New location: $location")
        mLocation = location
        val locationtext: CharSequence = getLocationText(mLocation)
        Log.v("seyuuuun", "위치확인 " + locationtext.toString())

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            if (calDistance(mLocation!!)) {
                Log.e("jieun", "이 지점에 300m 안이라 $locationtext")
                //receiver()
                placeCalendar.timeInMillis
                Log.v("seyuuuun", "장소 알람 시간 확인 : " + placeCalendar.get(Calendar.HOUR_OF_DAY))
                Log.v("seyuuuun", "장소 알람 시간 확인 : " + placeCalendar.get(Calendar.MINUTE))
                Log.v("seyuuuun", "장소 알람 시간 확인 : " + placeCalendar.get(Calendar.SECOND))
                setPlaceAlarm(placeCalendar)
            }

            mNotificationManager!!.notify(
                NOTIFICATION_ID,
                getNotification()
            )
        }


    }

    fun calDistance(location: Location): Boolean {
        //장소 바꿔놓음
        val curLatitude = 37.582431
        val curLongitude = 127.009425
        val theta: Double
        var dist: Double
        theta = curLongitude - location.longitude
        dist =
            sin(deg2rad(curLatitude)) * sin(deg2rad(location.latitude)) + (cos(
                deg2rad(curLatitude)
            )
                    * cos(deg2rad(location.latitude)) * cos(deg2rad(theta)))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344 // 단위 mile 에서 km 변환.
        dist *= 1000.0 // 단위  km 에서 m 로 변환
        //Log.e("jieun", "확인중 $dist")
        Log.v("seyuuuun", "거리 : $dist")
        return dist <= 300
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private fun deg2rad(deg: Double): Double {
        return (deg * Math.PI / 180.0)
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private fun rad2deg(rad: Double): Double {
        return (rad * 180.0 / Math.PI)
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS   //업데이트 간격 시간 설정
        mLocationRequest!!.fastestInterval =
            FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS  //업데이트 간격 시간 설정
        mLocationRequest!!.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY //정밀도를 높이기위한 GPS가 우선적으로 사용
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        val service: LocationUpdatesService
            get() = this@LocationUpdatesService  //getService호출
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The [Context].
     */
    fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        private const val PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationupdatesforegroundservice"
        private val TAG = LocationUpdatesService::class.java.simpleName

        /**
         * The name of the channel for notifications.
         */
        private const val CHANNEL_ID = "알람"
        private const val CHANNEL_NAME = "알림채널"
        private const val CHANNEL_DESCRITION = "알림채널 리시버"
        const val ACTION_BROADCAST =
            "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION =
            "$PACKAGE_NAME.location"
        private const val EXTRA_STARTED_FROM_NOTIFICATION =
            PACKAGE_NAME +
                    ".started_from_notification"
        private const val EXTRA_START_FROM_NOTIFICATION =
            PACKAGE_NAME +
                    ".started_from_notification"

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

        /**
         * The fastest rate for active location updates. Updates will never be more frequent
         * than this value.
         */
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2

        /**
         * The identifier for the notification displayed for the foreground service.
         */
        private val NOTIFICATION_ID_NOTIFICATION = 123
        private val NOTIFICATION_ID = 12345678
    }
}
