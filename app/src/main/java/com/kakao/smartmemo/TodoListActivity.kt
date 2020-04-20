package com.kakao.smartmemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Data.DayData
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.View.MainActivity
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.DayRepeatAdapter
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.PlaceListAdapter
import kotlinx.android.synthetic.main.alarm_settings_place.*
import kotlinx.android.synthetic.main.alarm_settings_time.*
import kotlinx.android.synthetic.main.time_location_settings.*
import java.lang.String.format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TodoListActivity : AppCompatActivity() {
    private lateinit var myToolbar: Toolbar
    private lateinit var alarmswitch_time : Switch
    private lateinit var alarmswitch_location : Switch
    private lateinit var timebtn: ImageButton
    private lateinit var placebtn: ImageButton
    private lateinit var placelistview : ListView
    private lateinit var daylistview: RecyclerView
    private lateinit var timeSpinner : Spinner
    private lateinit var placeSpinner : Spinner
    private lateinit var savebtn : Button
    private var time  : String = ""

    private var placeList = arrayListOf<PlaceData>(PlaceData("연세병원"))
    private var dayList = mutableListOf<DayData>(DayData("월"), DayData("화"), DayData("수"), DayData("목"), DayData("금"), DayData("토"), DayData("일"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        var ringingAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.again_time, android.R.layout.simple_spinner_dropdown_item)
        val todostub_time = stub_alarm_time
        val view_time = todostub_time.inflate()
        todostub_time.visibility = GONE

        val todostub_location = stub_alarm_location
        val view_location = todostub_location.inflate()
        todostub_location.visibility = GONE
        val textview_Time = textView_time_show

        var dateSettingInTime = view_time.findViewById(R.id.date_layout) as ConstraintLayout
        var dateSettingInPlace = view_location.findViewById(R.id.date_layout) as ConstraintLayout
        var dateTextInTime = view_time.findViewById<TextView>(R.id.textView4)
        var dateTextInPlace = view_location.findViewById<TextView>(R.id.textView4)

        // Toolbar달기
        myToolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(myToolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        alarmswitch_time = switch_time
        alarmswitch_location = switch_location
        timebtn = view_time.findViewById(R.id.btn_time_settings) //시간설정버튼
        daylistview = view_time.findViewById(R.id.listview_day_repeat)  //요일반복 나오는 recyclerview

        placebtn = view_location.findViewById(R.id.btn_place_choice) //장소선택 버튼
        placelistview = view_location.findViewById(R.id.listview_place) //장소선택시 나오는 listview
        

        dateSettingInTime.setOnClickListener {
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateTextInTime.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
            }
            val dateDia = DatePickerDialog(this,dateListener, LocalDate.now().year,LocalDate.now().monthValue-1,LocalDate.now().dayOfMonth)
            dateDia.show()
        }

        dateSettingInPlace.setOnClickListener {
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateTextInPlace.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
            }
            val dateDia = DatePickerDialog(this,dateListener, LocalDate.now().year,LocalDate.now().monthValue-1,LocalDate.now().dayOfMonth)
            dateDia.show()
        }

        //시간알림 반복시간 설정
        timeSpinner = repeat_time_spinner
        timeSpinner.adapter = ringingAdapter
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {   }
        }

        //장소 알림 반복시간 설정
        placeSpinner = repeat_place_spinner
        placeSpinner.adapter = ringingAdapter
        placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {   }
        }

        alarmswitch_time.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todostub_time.visibility = VISIBLE
                //val cal = Calendar.getInstance()
                timebtn.setOnClickListener {
                    var listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        var hour = 0
                        var am_pm = "오전"
                        //cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        //cal.set(Calendar.MINUTE, minute)
                        var m = minute.toString()
                        if (hourOfDay == 0) {
                            am_pm = "오전"
                            hour = 12
                        }
                        if (hourOfDay >= 12) {
                            am_pm = "오후"
                            hour = hourOfDay % 12
                            if (hour == 0) {
                                hour = 12
                            }

                        }
                        else{
                            hour = hourOfDay
                        }
                        if (minute == 0) {
                            m = "00"
                        }
                        textview_Time.text = "${am_pm} ${hour} : ${m} "
                        //time = SimpleDateFormat("HH:mm").format(cal.time)
                        //Log.v("seyuuuun", time)
                    }
                    val dialog = TimePickerDialog(this,listener,12,0,false)
                    dialog.show()
                }
            } else {
                todostub_time.visibility = GONE
            }
        }

        //요일반복 선택시 나오는 recyclerview 어댑터
        daylistview.adapter = DayRepeatAdapter(this, dayList)
        daylistview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) //가로 recyclerview
        daylistview.scrollToPosition(0)  //recyclerview position 맨앞으로
        
        alarmswitch_location.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todostub_location.visibility = VISIBLE
                placebtn.setOnClickListener(View.OnClickListener {
                    val placechoiceIntent = Intent(it.context, MainActivity::class.java)
                    this.startActivity(placechoiceIntent)
                })
            } else {
                todostub_location.visibility = GONE
            }
        }

        //장소선택시 나오는 listview 어댑터
        placelistview.adapter = PlaceListAdapter(this, placeList)

        savebtn = this.findViewById(R.id.saveTodoAlarmButton)
        savebtn.setOnClickListener(View.OnClickListener {
            time_alarm(it)
            finish()
        })
    }

    //툴바의 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                //뒤로가기 클릭시 종 off ->실행이안되 고쳐야함.
                val view = LayoutInflater.from(this).inflate(R.layout.todo_list_item, null)
                Log.v("seyuuuun", "종 꺼짐")
                val btn_todo = view.findViewById(R.id.btn_todo) as Button
                btn_todo.setBackgroundResource(R.drawable.bell_icon)
                Log.v("seyuuuun", "종 꺼짐22")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun time_alarm(v: View) { //시간 알림
        var date_now = LocalDate.now() //현재 시간받아오기.
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val now = date_now.format(formatter)

        val time_now = "20:18"
        var date_settings = time_now.format(formatter) //설정된 시간 받아오기.
        if(date_now.toString() == date_settings) { //설정한 시간과 현재시간이 같을 경우
            noti(v)
        }

    }

    //알림 구현
    private fun noti(v: View) {
        val notificationManager = createNotificationChannel()

        val resultIntent = Intent(this, MainActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Bitmap IconNoti = BitmapFactory.decodeResource(getResources(), R.drawable.location_icon3);
        val customNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent) // 알림을 눌렀을때 실행할 작업 인텐트 설정
            .setWhen(System.currentTimeMillis()) //miliSecond단위로 넣어주면 내부적으로 파싱함.
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setFullScreenIntent(pendingIntent,true) //헤드업알림
            .setNumber(999) //확인하지않은 알림 개수 설정

        val contentview = RemoteViews(packageName, R.layout.location_notification)
        contentview.setTextViewText(R.id.notification_Title, "notification")
        contentview.setOnClickPendingIntent(R.id.later_notification, pendingIntent)
        contentview.setOnClickPendingIntent(R.id.cancel_notification, pendingIntent)
        customNotification.setContent(contentview)

        notificationManager?.notify(0, customNotification.build())

    }

    private fun createNotificationChannel() : NotificationManager {
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = CHANNEL_DESCRITION }

            notificationManager.createNotificationChannel(serviceChannel)
            return notificationManager
        }
        return notificationManager
    }

    companion object {
        val CHANNEL_ID = "테스트 "
        val CHANNEL_NAME = "알림채널 이름"
        val CHANNEL_DESCRITION = "알림채널 설명"
    }
}