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
import com.kakao.smartmemo.Contract.AddTodoContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.Object.UserObject
import com.kakao.smartmemo.Presenter.AddTodoPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Receiver.AlarmReceiver
import com.kakao.smartmemo.Receiver.DeviceBootAlarmReceiver
import com.kakao.smartmemo.Receiver.DeviceBootTodoReceiver
import com.kakao.smartmemo.Receiver.TodoReceiver
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.PlaceListAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AddTodo : AppCompatActivity(), AddTodoContract.View {

    private lateinit var todoToolBar: Toolbar
    private lateinit var presenter : AddTodoContract.Presenter
    //private lateinit var userpresenter : MemberDataPresenter
    private lateinit var titleEdit: EditText
    private lateinit var selectGroupBtn : Button
    private var groupName: String = ""
    private var groupId: String = ""
    private var groupColor: Long = 0
    private lateinit var todoStubTime: ViewStub
    private lateinit var todoStubLocation: ViewStub
    private lateinit var viewTime : View
    private lateinit var viewLocation : View
    private lateinit var timeSwitch : Switch
    private lateinit var timeDateLayout: ConstraintLayout
    private lateinit var timeDateText : TextView
    private lateinit var timeLayout: ConstraintLayout
    private lateinit var timeText: TextView
    private lateinit var timeAgainText : TextView
    private lateinit var timeSpinner : Spinner // 시간 다시 울림 주기
    private var timePosition = 0
    private lateinit var placeSwitch : Switch
    private lateinit var placeDateLayout : ConstraintLayout
    private lateinit var placeDateText : TextView
    private lateinit var placeSpinner : Spinner
    private var placePosition = 0
    private lateinit var placeAgainText: TextView
    // private lateinit var placeNames : String -> 선택한 장소 이름
    private lateinit var timebtn: ImageButton
    private lateinit var placebtn: ImageButton
    private lateinit var placeLayout : ConstraintLayout
    private lateinit var placeListView : ListView
    private lateinit var savebtn : Button
    private val timeCalendar = Calendar.getInstance()
    private val placeCalendar = Calendar.getInstance()
    private val todoCalendar = Calendar.getInstance()
    private var timeAlarmId = ""
    private var placeAlarmId = ""
    private var settingsTimeMinutes = 0
    private var settingsPlaceMinutes = 0
    private var todoHour = 0
    private var todoMinute = 0
    private var currentHour = 0
    private var currentMinute = 0
    val interval = AlarmManager.INTERVAL_DAY
    private var notifyTime = false
    val date: LocalDateTime = LocalDateTime.now()
    var hour = 0
    var amPm = "오전"
    var min = 0
    private lateinit var data : TodoData


    private var latitude: Double? = null
    private var longitude: Double? = null
    private var address: String? = null

    private var placeList = arrayListOf(PlaceData("연세병원"), PlaceData("학교"), PlaceData("집"))

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
        selectGroupBtn = findViewById(R.id.todo_select_group)
        timeSwitch = findViewById(R.id.switch_time)
        todoStubTime = findViewById(R.id.stub_alarm_time)
        todoStubLocation = findViewById(R.id.stub_alarm_location)

        placeSwitch = findViewById(R.id.switch_location)
        savebtn = findViewById(R.id.saveTodoAlarmButton)

        viewTime = todoStubTime.inflate()
        todoStubTime.visibility = GONE
        timeDateLayout = viewTime.findViewById(R.id.time_date_layout) as ConstraintLayout
        timeDateText = viewTime.findViewById(R.id.time_date_text)
        timeLayout = viewTime.findViewById(R.id.time_layout)
        timeText = viewTime.findViewById(R.id.time_text)
        timeSpinner = viewTime.findViewById(R.id.repeat_time_spinner)
        timeAgainText = viewTime.findViewById(R.id.time_again_text)

        viewLocation = todoStubLocation.inflate()
        todoStubLocation.visibility = GONE
        placeDateLayout = viewLocation.findViewById(R.id.place_date_layout) as ConstraintLayout
        placeLayout = viewLocation.findViewById(R.id.place_layout)
        placeDateText = viewLocation.findViewById<TextView>(R.id.place_date_text)
        placeSpinner = viewLocation.findViewById(R.id.repeat_place_spinner)
        placeAgainText = viewLocation.findViewById(R.id.ring_again_place)

        var timeAgainAdapter = ArrayAdapter.createFromResource(applicationContext,
            R.array.again_time, android.R.layout.simple_spinner_dropdown_item)

        timebtn = viewTime.findViewById(R.id.btn_time_settings) //시간설정버튼
        timebtn.isClickable = false

        placebtn = viewLocation.findViewById(R.id.btn_place_choice) //장소선택 버튼
        placeListView = viewLocation.findViewById(R.id.listview_place) //장소선택시 나오는 listview

        timeDateLayout.setOnClickListener { //시간 날짜 설정
            var year = LocalDate.now().year
            var month = LocalDate.now().monthValue-1
            var day = LocalDate.now().dayOfMonth
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                timeDateText.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
                timeCalendar.set(Calendar.YEAR, year) // 년
                timeCalendar.set(Calendar.MONTH, month) // 월
                timeCalendar.set(Calendar.DATE, dayOfMonth) // 일
            }
            val dateDia = DatePickerDialog(this,dateListener, year, month, day)
            dateDia.show()
        }

        placeDateLayout.setOnClickListener { //장소 날짜 설정
            var year = LocalDate.now().year
            var month = LocalDate.now().monthValue-1
            var day = LocalDate.now().dayOfMonth
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                placeDateText.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
                placeCalendar.set(Calendar.YEAR, year) //년
                placeCalendar.set(Calendar.MONTH, month) //월
                placeCalendar.set(Calendar.DATE, dayOfMonth) //일
            }
            val dateDia = DatePickerDialog(this,dateListener, year, month, day)
            dateDia.show()
        }

        //시간알림 반복시간 설정
        timeSpinner.adapter = timeAgainAdapter
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    settingsTimeMinutes = 0
                } else if(position == 1) {
                    settingsTimeMinutes = 1
                } else if( position == 2) {
                    settingsTimeMinutes = 3
                } else if( position == 3) {
                    settingsTimeMinutes = 5
                } else if( position == 4) {
                    settingsTimeMinutes = 10
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }

        //장소 알림 반복시간 설정
        placeSpinner.adapter = timeAgainAdapter
        placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    settingsPlaceMinutes = 0
                } else if(position == 1) {
                    settingsPlaceMinutes = 1
                } else if( position == 2) {
                    settingsPlaceMinutes = 3
                } else if( position == 3) {
                    settingsPlaceMinutes = 5
                } else if( position == 4) {
                    settingsPlaceMinutes = 10
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }

        timeSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                notifyTime = true // 알람 켬.
                todoStubTime.visibility = VISIBLE
                timeCalendar.timeInMillis
                timeLayout.setOnClickListener(timeDialogClickListener)
                currentHour = timeCalendar.get(Calendar.HOUR_OF_DAY)
                currentMinute = timeCalendar.get(Calendar.MINUTE)
                if(timeCalendar.get(Calendar.HOUR_OF_DAY) < 12) {
                    amPm = "오전"
                }else {
                    amPm = "오후"
                    currentHour -=  12
                }
                timeText.text = "${amPm} ${currentHour} : ${String.format("%02d", currentMinute)}"
            } else {
                timeDateText.text = "[기본] 날짜 미설정"
                settingsTimeMinutes = 0
                todoStubTime.visibility = GONE
                notifyTime = false //알람 끔.
            }
        }

        placeSwitch.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todoStubLocation.visibility = VISIBLE
                placeLayout.setOnClickListener(View.OnClickListener {
                    val placechoiceIntent = Intent(it.context, PlaceAlarmDetailActivity::class.java)
                    placechoiceIntent.putExtra("longitude", longitude)
                    placechoiceIntent.putExtra("latitude", latitude)
                    placechoiceIntent.putExtra("address", address)
                    this.startActivity(placechoiceIntent)
            })
                notifyTime = true // 알람 켬.
            } else {
                placeDateText.text = "[기본] 날짜 미설정"
                settingsPlaceMinutes = 0
                placeList.clear()
                todoStubLocation.visibility = GONE
                notifyTime = false //알람 끔.
            }
        }

        //장소선택시 나오는 listview 어댑터
        var placeListAdapter = PlaceListAdapter(this, placeList)
        placeListView.adapter = placeListAdapter
        presenter.setTodoPlaceAdapterModel(placeListAdapter)
        presenter.setTodoPlaceAdapterView(placeListAdapter)

        if (intent.hasExtra("todoData")) {
            data = intent.getParcelableExtra("todoData")
            when(data.timeAgain) {
                0 -> timePosition = 0
                1 -> timePosition = 1
                3 -> timePosition = 2
                5 -> timePosition = 3
                10 -> timePosition = 4
            }
            when(data.placeAgain) {
                0 -> placePosition = 0
                1 -> placePosition = 1
                3 -> placePosition = 2
                5 -> placePosition = 3
                10 -> placePosition = 4
            }

            groupId = data.groupId
            groupColor = data.groupColor
            titleEdit.setText(data.title)
            selectGroupBtn.text = data.groupName
            timeSwitch.isChecked = data.setTimeAlarm
            timeDateText.text = data.timeDate
            timeText.text = data.timeTime
            settingsTimeMinutes = data.timeAgain
            timeSpinner.adapter = timeAgainAdapter
            timeSpinner.setSelection(timePosition, true)

            placeSwitch.isChecked = data.setPlaceAlarm
            placeDateText.text = data.placeDate
            settingsPlaceMinutes = data.placeAgain
            placeSpinner.adapter = timeAgainAdapter
            placeSpinner.setSelection(placePosition, true)

            savebtn.setOnClickListener {
                when {
                    titleEdit.text.isEmpty() -> {
                        Toast.makeText(applicationContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        var todoData = TodoData(
                            titleEdit.text.toString(),
                            data.groupName,
                            data.groupId,
                            data.groupColor,
                            data.todoId,
                            data.timeAlarmId,
                            timeSwitch.isChecked,
                            "${timeDateText.text}",
                            timeText.text.toString(),
                            settingsTimeMinutes,
                            data.placeAlarmId,
                            placeSwitch.isChecked,
                            "${placeDateText.text}",
                            settingsPlaceMinutes,
                            "한성대학교",
                            "0.0",
                            "0.0"
                        )
                        presenter.addTodo(todoData)
                        if (timeSwitch.isChecked) {
                            // 지정한 시간에 울리게 알람을 세팅
                            setTimeLocationAlarm(notifyTime, timeCalendar, settingsTimeMinutes)
                        }else {
                            unsetTimeLocationAlarm() //알람 해제
                        }

                        todoAlarm()
                        receiverData()
                        finish()
                    }
                }
            }
        } else {
            savebtn.setOnClickListener {
                when {
                    groupName == "" -> {
                        Toast.makeText(applicationContext, "그룹을 선택해주세요", Toast.LENGTH_SHORT).show()
                    }
                    titleEdit.text.isEmpty() -> {
                        Toast.makeText(applicationContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        timeAlarmId = "time" + System.currentTimeMillis()
                        placeAlarmId = "place" + System.currentTimeMillis()
                        var todoData = TodoData(
                            titleEdit.text.toString(),
                            groupName,
                            groupId,
                            0,
                            "",
                            "${timeAlarmId}",
                            timeSwitch.isChecked,
                            "${timeDateText.text}",
                            timeText.text.toString(),
                            settingsTimeMinutes,
                            "${placeAlarmId}",
                            placeSwitch.isChecked,
                            "${placeDateText.text}",
                            settingsPlaceMinutes,
                            "한성대학교",
                            "0.0",
                            "0.0"
                        )
                        presenter.addTodo(todoData)
                        if (timeSwitch.isChecked) {
                            // 지정한 시간에 울리게 알람을 세팅
                            setTimeLocationAlarm(notifyTime, timeCalendar, settingsTimeMinutes)
                        }

                        todoAlarm()
                        receiverData()

                        //if (placeSwitch.isChecked) {
                        //      placeCalendar.set(Calendar.MINUTE, Calendar.MINUTE+settingsPlaceMinutes)
                        //      setTimeLocationAlarm(placeCalendar, settingsPlaceMinutes)
                        //}

                        finish()
                    }
                }

            }
        }

        selectGroupBtn.setOnClickListener {
            selectGroup()
        }

        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        address = intent.getStringExtra("address")
        Log.e("check", "latitude = $latitude, longitude = $longitude, address = $address")
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
            min = minute
            if (hourOfDay == 0) {
                amPm = "오전"
                hour = 12
            }
            if (hourOfDay >= 12) {
                amPm = "오후"
                hour = hourOfDay % 12
                if (hour == 0) {
                    hour = 12
                }
            } else{
                hour = hourOfDay
            }

            min = if (minute == 0) {
                0
            } else {
                minute
            }
            timeText.text = "${amPm} ${hour} : ${String.format("%02d", min)}"
            timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay) // 시
            timeCalendar.set(Calendar.MINUTE, minute) // 분
            timeCalendar.set(Calendar.SECOND, 0) // 초

            val currentTime = System.currentTimeMillis()
            var settingTime = timeCalendar.timeInMillis
            if (currentTime > settingTime) {
                timeCalendar.timeInMillis += interval //지정시간이 지난 경우 interval을 추가해줌.
            }
        }

        val dialog = TimePickerDialog(this, listener,12,0,false)
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
                selectGroupBtn.text = items[which]
                groupName = items[which].toString()
                groupId = groupIds[which].toString()
            })
            .show()
    }

    private fun receiverData() {
        if (intent.hasExtra("다시알림") or intent.hasExtra("알림해제")) {
            var repeat = intent.getIntExtra("다시알림", 5)
            Log.v("seyuuuun", "repeat in" + repeat)
            val cancel = intent.getBooleanExtra("알림해제", true)
            Log.v("seyuuuun", "cancel in" + cancel)
        } else {

        }
    }

    private fun todoAlarm() {
        val todoTime = UserObject.kakao_alarm_time
        if(!todoTime.equals("")) { //안에 아무것도 없을시에
            var Todo = todoTime.split(" ")
            when (Todo.get(0)) { //오전 오후 구분
                "오후" ->
                    todoHour = Todo.get(1).toInt() + 12
                "오전" ->
                    todoHour = Todo.get(0).toInt()
            }
            todoMinute = Todo.get(3).toInt() //분

            todoCalendar.set(Calendar.HOUR_OF_DAY, todoHour)
            todoCalendar.set(Calendar.MINUTE, todoMinute)
            todoCalendar.set(Calendar.SECOND, 0)
            val currentTime = System.currentTimeMillis()
            var settingTime = todoCalendar.timeInMillis
            val interval = AlarmManager.INTERVAL_DAY
            if (currentTime > settingTime) {
                todoCalendar.timeInMillis += interval //지정시간이 지난 경우 interval을 추가해줌.
            }
            setTodoAlarm(todoCalendar)
        } else {
            unsetTodoAlarm()
        }
    }

    private fun setTimeLocationAlarm(notifyTime : Boolean, calendar: Calendar, settingTime: Int) {  //시간알람, 장소알람
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootAlarmReceiver::class.java)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, 2, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val interval = 1000*60*settingTime

        if(notifyTime) { //알람을 허용했다면
            if(alarmManager != null) {
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
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        }
    }

    private fun unsetTimeLocationAlarm() {
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootAlarmReceiver::class.java)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, 2, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(PendingIntent.getBroadcast(this, 2, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)!=null && alarmManager!=null) {
            alarmManager.cancel(pendingIntent)
        }
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }

    private fun setTodoAlarm(calendar: Calendar) {  //시간알람
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootTodoReceiver::class.java)
        val alarmIntent = Intent(this, TodoReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            //부팅후 실행되는 리시버 사용가능하게 설정함.
            pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    private fun unsetTodoAlarm() {
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootTodoReceiver::class.java)
        val todoalarmIntent = Intent(this, TodoReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, 1, todoalarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(PendingIntent.getBroadcast(this, 1, todoalarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)!=null && alarmManager!=null) { //알림 해제
            alarmManager.cancel(pendingIntent)
        }
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
    }
}