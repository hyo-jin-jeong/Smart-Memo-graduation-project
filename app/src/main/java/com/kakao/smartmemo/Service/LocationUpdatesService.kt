package com.kakao.smartmemo.Service

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Receiver.DeviceBootPlaceReceiver
import com.kakao.smartmemo.Receiver.PlaceReceiver
import com.kakao.smartmemo.Utils.Utils.getLocationText
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

    private var PlaceNotificationID  = 0
    private val placeCalendar = Calendar.getInstance()

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
    private var allSelectedPlace = arrayListOf<PlaceData>()
    private var handler = Handler()
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

        allSelectedPlace = intent.getParcelableArrayListExtra("allSelectedPlaceList")

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
        stopForeground(true)  //상태 표시줄 알림 제거
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent) { //onUnbind일때
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        stopForeground(true) //상태 표시줄 알림 제거
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean { //bindService종료 꼭 해줘야 제대로 종료됨!

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && requestingLocationUpdates(this)) {
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
    fun requestLocationUpdates(allSelectedPlaceList: ArrayList<PlaceData>) {
        setRequestingLocationUpdates(this, true)

        val intent = Intent(applicationContext, LocationUpdatesService::class.java)
        intent.putExtra("allSelectedPlaceList", allSelectedPlaceList)
//        startService(Intent(applicationContext, LocationUpdatesService::class.java))
        startService(intent)
        try {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            setRequestingLocationUpdates(this, false)
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    private fun removeLocationUpdates() {
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            setRequestingLocationUpdates(this, false)
            stopSelf()
        } catch (unlikely: SecurityException) {
            setRequestingLocationUpdates(this, true)
        }
    }

    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
    private fun getNotification(): Notification {
        val intent = Intent(this, LocationUpdatesService::class.java)
        val text: CharSequence = getLocationText(mLocation)
        val contentText = "앱의 장소 알람 기능을 사용하시려 알람을 유지해주세요.\n이 알림을 끄면 장소 알림이 제대로 실행되지 않을 수 있습니다."

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

        val contentview = RemoteViews(applicationContext.packageName, R.layout.custom_notif)
        contentview.setTextViewText(R.id.notification_Title, "백그라운드에서 대기 중")
        contentview.setTextViewText(R.id.textView_alarm, "앱의 장소 알람 기능을 사용하시려면 알람을 유지해주세요.\n이 알림을 끄면 장소 알림이 제대로 실행되지 않을 수 있습니다.")
        val builder =
            NotificationCompat.Builder(this)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(activityPendingIntent)
                .setContentText(contentText)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.app_icon)
                .setWhen(System.currentTimeMillis())
                .setContent(contentview)

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = CHANNEL_DESCRITION }
            serviceChannel.vibrationPattern = longArrayOf(0) //진동 무음 설정
            serviceChannel.enableVibration(true) //
            mNotificationManager?.createNotificationChannel(serviceChannel)

            builder.setChannelId(CHANNEL_ID) // Channel ID
        }
        return builder.build()
    }

    private fun setPlaceAlarm(calendar: Calendar, placeData: PlaceData) {
        val text: CharSequence = getLocationText(mLocation)
        var title:String
        var date :String
        var again :Int = 0
        val pm = this.packageManager
        val placereceiver = ComponentName(this, DeviceBootPlaceReceiver::class.java)
        val placealarmIntent = Intent(this, PlaceReceiver::class.java)

        var firebaseTodo = FirebaseDatabase.getInstance().reference.child("Todo")
        firebaseTodo.child(placeData.id).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                title = dataSnapshot.child("title").value.toString()
                dataSnapshot.child("PlaceAlarm").children.forEach {
                    if(it.key == "placeAgain"){
                        again = it.value.toString().toInt()
                    }else if(it.key =="placeDate"){
                        date = it.value.toString()
                    }
                }
                placealarmIntent.putExtra("todoTitle", title)
                placealarmIntent.putExtra("todoPlace", placeData.place)
                placealarmIntent.putExtra("todoText", text)
                placealarmIntent.putExtra("todoId", placeData.id.toInt()) //reqeustcode 때문에 넣어준 것!!
                setAlarm(placealarmIntent,placeData,calendar, again)
            }
        })
    }
    private fun setAlarm(intent: Intent,placeData: PlaceData,calendar: Calendar, again: Int){

        val pendingIntent = PendingIntent.getBroadcast(applicationContext, placeData.placeId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intervalTime = 1000*60*again

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                if(again != 0) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intervalTime.toLong(), pendingIntent)
                }
            }
        }
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                        //Log.v("seyuuuun", "Location: " + mLocation)
                    } else {
                    }
                }
        } catch (unlikely: SecurityException) {
        }
    }

    private fun onNewLocation(location: Location) {
        mLocation = location
        val locationtext: CharSequence = getLocationText(mLocation)

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)

        val placeIntent = Intent(applicationContext, AddTodo::class.java)
        var placeItems : ArrayList<PlaceData> = arrayListOf()

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {

            for (place in allSelectedPlace) {
                val location = Location("")
                location.longitude = place.longitude
                location.latitude = place.latitude
                if (calDistance(location, mLocation!!)) {

                    placeCalendar.timeInMillis //날짜DB에서 불러오면 적용시켜줘야함
                    PlaceNotificationID = ((System.currentTimeMillis()/1000).toInt()) * (allSelectedPlace.indexOf(place) +1)
                    setPlaceAlarm(placeCalendar, place)
                    placeItems.add(place)
                }
            }

            for(placeItem in placeItems) {
                allSelectedPlace.remove(placeItem)
            }

            mNotificationManager!!.notify(
                NOTIFICATION_ID,
                getNotification()
            )
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    fun calDistance(placeAlarmLocation: Location, curLocation: Location): Boolean {

        val curLatitude = placeAlarmLocation.latitude
        val curLongitude = placeAlarmLocation.longitude

        val theta: Double
        var dist: Double
        theta = curLongitude - curLocation.longitude
        dist = sin(deg2rad(curLatitude)) * sin(deg2rad(curLocation.latitude)) + (cos(
            deg2rad(curLatitude))* cos(deg2rad(curLocation.latitude)) * cos(deg2rad(theta)))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344 // 단위 mile 에서 km 변환.
        dist *= 1000.0 // 단위  km 에서 m 로 변환
        return dist <= 100
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
        private const val CHANNEL_ID = "백그라운드"
        private const val CHANNEL_NAME = "백그라운드채널"
        private const val CHANNEL_DESCRITION = "백그라운드채널 리시버"
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