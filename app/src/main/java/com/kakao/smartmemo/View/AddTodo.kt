package com.kakao.smartmemo.View

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
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
import com.kakao.smartmemo.Contract.AddTodoContract
import com.kakao.smartmemo.Data.DayData
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.Presenter.AddTodoPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Receiver.AlarmReceiver
import com.kakao.smartmemo.Receiver.DeviceBootAlarmReceiver
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.DayRepeatAdapter
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.PlaceListAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddTodo : AppCompatActivity(), AddTodoContract.View {

    private lateinit var todoToolBar: Toolbar
    private lateinit var presenter : AddTodoContract.Presenter
    private lateinit var titleEdit: EditText
    private lateinit var selectGroup : Button
    private lateinit var groupName: String
    private lateinit var groupId: String
    private lateinit var todoStubTime: ViewStub
    private lateinit var todoStubLocation: ViewStub
    private lateinit var viewTime : View
    private lateinit var viewLocation : View
    private lateinit var timeSwitch : Switch
    private lateinit var dayList: MutableList<DayData>
    private lateinit var timeDateLayout: ConstraintLayout
    private lateinit var timeDateText : TextView
    private lateinit var timeLayout: ConstraintLayout
    private lateinit var timeText: TextView
    private lateinit var timeSpinner : Spinner // 시간 다시 울림 주기
    private lateinit var placeSwitch : Switch
    private lateinit var placeDateLayout : ConstraintLayout
    private lateinit var placeDateText : TextView
    private lateinit var placeSpinner : Spinner
    // private lateinit var placeNames : String -> 선택한 장소 이름
    private lateinit var timebtn: ImageButton
    private lateinit var placebtn: ImageButton

    private lateinit var placelistview : ListView
    private lateinit var daylistview: RecyclerView
    private lateinit var savebtn : Button
    private val calendar = Calendar.getInstance()
    private val placeCalendar = Calendar.getInstance()
    private var notifyTime = false
    val date: LocalDateTime = LocalDateTime.now()

    private var placeList = arrayListOf<PlaceData>(PlaceData("연세병원"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        // Toolbar달기
        todoToolBar = findViewById(R.id.settings_toolbar)
        todoToolBar.title = resources.getString(R.string.todo)
        setSupportActionBar(todoToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // presenter 초기화
        presenter = AddTodoPresenter(this)
        titleEdit = findViewById(R.id.edit_todolist)
        selectGroup = findViewById(R.id.todo_select_group)
        timeSwitch = findViewById(R.id.switch_time)
        todoStubTime = findViewById<ViewStub>(R.id.stub_alarm_time)
        todoStubLocation = findViewById<ViewStub>(R.id.stub_alarm_location)

        placeSwitch = findViewById(R.id.switch_location)

        viewTime = todoStubTime.inflate()
        todoStubTime.visibility = GONE
        timeDateLayout = viewTime.findViewById(R.id.time_date_layout) as ConstraintLayout
        timeDateText = viewTime.findViewById<TextView>(R.id.time_date_text)
        timeLayout = viewTime.findViewById(R.id.time_layout)
        timeText = viewTime.findViewById(R.id.time_text)
        timeSpinner = viewTime.findViewById(R.id.repeat_time_spinner)

        viewLocation = todoStubLocation.inflate()
        todoStubLocation.visibility = GONE
        placeSpinner = viewLocation.findViewById(R.id.repeat_place_spinner)
        placeDateLayout = viewLocation.findViewById(R.id.place_date_layout) as ConstraintLayout
        placeDateText = viewLocation.findViewById<TextView>(R.id.place_date_text)

        dayList = mutableListOf<DayData>(DayData("월"), DayData("화"), DayData("수"), DayData("목"), DayData("금"), DayData("토"), DayData("일"))

        selectGroup.setOnClickListener {
            selectGroup()
        }

        var ringingAdapter = ArrayAdapter.createFromResource(applicationContext,
            R.array.again_time, android.R.layout.simple_spinner_dropdown_item)

        //현재시간 가져오기
        val dateFormatter = DateTimeFormatter.ISO_DATE
        val timeFormatter = DateTimeFormatter.ISO_TIME
        val currentDate = date.format(dateFormatter) //현재 날짜
        val currentTime = date.format(timeFormatter) //현재 시간

        timebtn = viewTime.findViewById(R.id.btn_time_settings) //시간설정버튼
        daylistview = viewTime.findViewById(R.id.listview_day_repeat)  //요일반복 나오는 recyclerview

        placebtn = viewLocation.findViewById(R.id.btn_place_choice) //장소선택 버튼
        placelistview = viewLocation.findViewById(R.id.listview_place) //장소선택시 나오는 listview

        timeDateLayout.setOnClickListener { //시간 날짜 설정
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                timeDateText.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DATE, dayOfMonth)
            }
            val dateDia = DatePickerDialog(this,dateListener, LocalDate.now().year,LocalDate.now().monthValue-1,LocalDate.now().dayOfMonth)
            dateDia.show()
        }

        placeDateLayout.setOnClickListener { //장소 날짜 설정
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                placeDateText.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
            }
            val dateDia = DatePickerDialog(this,dateListener, LocalDate.now().year,LocalDate.now().monthValue-1,LocalDate.now().dayOfMonth)
            dateDia.show()
        }

        //시간알림 반복시간 설정
        timeSpinner.adapter = ringingAdapter
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var settingsMinute = calendar.get(Calendar.MINUTE) //원래 알람 설정시간
                if(position == 1) {
                    calendar.set(Calendar.MINUTE, settingsMinute+1)
                } else if( position == 2) {
                    calendar.set(Calendar.MINUTE, settingsMinute+3)
                } else if( position == 3) {
                    calendar.set(Calendar.MINUTE, settingsMinute+5)
                } else if( position == 4) {
                    calendar.set(Calendar.MINUTE, settingsMinute+10)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {   }
        }

        //장소 알림 반복시간 설정
        placeSpinner.adapter = ringingAdapter
        placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var settingsMinute = placeCalendar.get(Calendar.MINUTE) //원래 알람 설정 시간
                if(position == 1) {
                    placeCalendar.set(Calendar.MINUTE, settingsMinute+1)
                } else if( position == 2) {
                    placeCalendar.set(Calendar.MINUTE, settingsMinute+3)
                } else if( position == 3) {
                    placeCalendar.set(Calendar.MINUTE, settingsMinute+5)
                } else if( position == 4) {
                    placeCalendar.set(Calendar.MINUTE, settingsMinute+10)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {   }
        }

        timeSwitch.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                notifyTime = true // 알람 켬.
                Toast.makeText(applicationContext, notifyTime.toString(), Toast.LENGTH_SHORT).show()
                todoStubTime.visibility = VISIBLE
                calendar.timeInMillis
                timeLayout.setOnClickListener(timeDialogClickListener)
            } else {
                todoStubTime.visibility = GONE
                notifyTime = false
                Toast.makeText(applicationContext, notifyTime.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        //요일반복 선택시 나오는 recyclerview 어댑터
        var dateAdapter = DayRepeatAdapter(this, dayList)
        daylistview.adapter = dateAdapter
        presenter.setTodoDateAdapterView(dateAdapter)
        presenter.setTodoDateAdapterModel(dateAdapter)
        daylistview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) //가로 recyclerview
        daylistview.scrollToPosition(0)  //recyclerview position 맨앞으로
        val DateList = dateAdapter.selectDate()

       /* for (i in 0.. DateList.size) { //size=0인 오류남.
            Log.v("seyuuuun", "DateList:" + DateList.get(i).toString())
        }*/

        placeSwitch.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todoStubLocation.visibility = VISIBLE
                placebtn.setOnClickListener(View.OnClickListener {
                    val placechoiceIntent = Intent(it.context, MainActivity::class.java)
                    this.startActivity(placechoiceIntent)
                })
            } else {
                todoStubLocation.visibility = GONE
            }
        }

        //장소선택시 나오는 listview 어댑터
        var placeListAdapter = PlaceListAdapter(this, placeList)
        placelistview.adapter = placeListAdapter
        presenter.setTodoPlaceAdapterModel(placeListAdapter)
        presenter.setTodoPlaceAdapterView(placeListAdapter)

        savebtn = findViewById(R.id.saveTodoAlarmButton)
        savebtn.setOnClickListener {
            var todoData
                    = TodoData(titleEdit.text.toString(), groupName, groupId, "time"+System.currentTimeMillis(), timeSwitch.isChecked, "", timeDateText.text.toString(), timeText.text.toString(), "",
            "place"+System.currentTimeMillis(), placeSwitch.isChecked, placeDateText.text.toString(), "", "한성대학교", 0.0, 0.0)
            presenter.addTodo(todoData)
            setTimeAlarm(calendar)
            finish()
        }
    }

    //툴바의 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var timeDialogClickListener = View.OnClickListener { view ->
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
            timeText.text = "${am_pm} ${hour} : ${m} "
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            var settingTime = calendar.timeInMillis
            val currentTime = System.currentTimeMillis()
            val interval = AlarmManager.INTERVAL_DAY

            if(currentTime > settingTime) {
                calendar.timeInMillis += interval
            }
            setTimeAlarm(calendar)
        }
        val dialog = TimePickerDialog(this,listener,12,0,false)
        dialog.show()
    }

    fun selectGroup() {
        var i = 0
        val items:Array<CharSequence> = Array(GroupObject.groupInfo.size) {""}
        val groupIds:Array<CharSequence> = Array(GroupObject.groupInfo.size) {""}

        GroupObject.groupInfo.forEach {
            groupIds[i] = it.key
            items[i] = it.value
            i++
        }
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
        listDialog.setTitle("그룹 선택")
            .setItems(items, DialogInterface.OnClickListener() { _, which ->
                selectGroup.text = items[which]
                groupName = items[which].toString()
                groupId = groupIds[which].toString()
            })
            .show()
    }

    private fun setTimeAlarm(calendar: Calendar) {


        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootAlarmReceiver::class.java)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(notifyTime) { //알람을 허용했다면
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