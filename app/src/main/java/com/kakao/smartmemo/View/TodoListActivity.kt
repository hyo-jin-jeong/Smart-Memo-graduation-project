package com.kakao.smartmemo.View

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.TodoSettingContract
import com.kakao.smartmemo.Data.DayData
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Presenter.TodoSettingPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Receiver.AlarmReceiver
import com.kakao.smartmemo.Receiver.DeviceBootReceiver
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.DayRepeatAdapter
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.PlaceListAdapter
import kotlinx.android.synthetic.main.alarm_settings_place.*
import kotlinx.android.synthetic.main.alarm_settings_time.*
import kotlinx.android.synthetic.main.time_location_settings.*
import java.time.LocalDate
import java.util.*

class TodoListActivity : AppCompatActivity(), TodoSettingContract.View {

    private lateinit var presenter : TodoSettingContract.Presenter
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
    private val calendar = Calendar.getInstance()
    var notify_time = false;

    private var placeList = arrayListOf<PlaceData>(PlaceData("연세병원"))
    private var dayList = mutableListOf<DayData>(DayData("월"), DayData("화"), DayData("수"), DayData("목"), DayData("금"), DayData("토"), DayData("일"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        presenter = TodoSettingPresenter(this)

        var ringingAdapter = ArrayAdapter.createFromResource(applicationContext,
            R.array.again_time, android.R.layout.simple_spinner_dropdown_item)
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
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DATE, dayOfMonth)
                Log.v("seyuuuun", calendar.get(Calendar.YEAR).toString())
                Log.v("seyuuuun", calendar.get(Calendar.MONTH).toString())
                Log.v("seyuuuun", calendar.get(Calendar.DATE).toString())
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
                notify_time = true // 알람 켬.
                Toast.makeText(applicationContext, notify_time.toString(), Toast.LENGTH_SHORT).show()
                todostub_time.visibility = VISIBLE
                calendar.timeInMillis
                timebtn.setOnClickListener {
                    var listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        var hour = 0
                        var am_pm = "오전"
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
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        setTimeAlarm(calendar)
                    }
                    val dialog = TimePickerDialog(this,listener,12,0,false)
                    dialog.show()
                }
            } else {
                todostub_time.visibility = GONE
                notify_time = false
                Toast.makeText(applicationContext, notify_time.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        //요일반복 선택시 나오는 recyclerview 어댑터
        var dateAdapter = DayRepeatAdapter(this, dayList)
        daylistview.adapter = dateAdapter
        presenter.setTodoDateAdapterView(dateAdapter)
        presenter.setTodoDateAdapterModel(dateAdapter)
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
        var placeListAdapter = PlaceListAdapter(this, placeList)
        placelistview.adapter = placeListAdapter
        presenter.setTodoPlaceAdapterModel(placeListAdapter)
        presenter.setTodoPlaceAdapterView(placeListAdapter)

        savebtn = this.findViewById(R.id.saveTodoAlarmButton)
        savebtn.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    //툴바의 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTimeAlarm(calendar: Calendar) {
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootReceiver::class.java)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(notify_time) { //알람을 허용했다면
            if(alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }

                //부팅후 실행되는 리시버 사용가능하게 설정함.
                pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            }
            else { // 알람을 허용하지 않았다면
                if(PendingIntent.getBroadcast(this, 0, alarmIntent, 0)!=null && alarmManager!=null) {
                    alarmManager.cancel(pendingIntent)
                }
                pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            }
        }
    }
}