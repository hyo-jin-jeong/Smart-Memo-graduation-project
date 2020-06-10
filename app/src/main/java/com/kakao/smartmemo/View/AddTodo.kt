package com.kakao.smartmemo.View

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.kakao.smartmemo.Contract.AddTodoContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Object.UserObject
import com.kakao.smartmemo.Presenter.AddAddTodoPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Receiver.AlarmReceiver
import com.kakao.smartmemo.Receiver.DeviceBootAlarmReceiver
import com.kakao.smartmemo.Receiver.DeviceBootTodoReceiver
import com.kakao.smartmemo.Receiver.TodoReceiver
import com.kakao.smartmemo.Service.LocationUpdatesService
import com.kakao.smartmemo.Utils.Utils
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.PlaceListAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AddTodo : AppCompatActivity(), AddTodoContract.View,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var todoToolBar: Toolbar
    private lateinit var presenter: AddTodoContract.Presenter
    private lateinit var titleEdit: EditText
    private lateinit var selectGroupBtn: Button
    private var groupName: String = ""
    private var groupId: String = ""
    private var groupColor: Long = 0
    private lateinit var todoStubTime: ViewStub
    private lateinit var todoStubLocation: ViewStub
    private lateinit var viewTime: View
    private lateinit var viewLocation: View
    private lateinit var timeSwitch: Switch
    private lateinit var timeDateLayout: ConstraintLayout
    private lateinit var timeDateText: TextView
    private lateinit var timeLayout: ConstraintLayout
    private lateinit var timeText: TextView
    private lateinit var timeAgainText: TextView
    private lateinit var timeSpinner: Spinner // 시간 다시 울림 주기
    private var timePosition = 0
    private lateinit var placeSwitch: Switch
    private lateinit var placeDateLayout: ConstraintLayout
    private lateinit var placeDateText: TextView
    private lateinit var placeSpinner: Spinner
    private var placePosition = 0
    private lateinit var placeAgainText: TextView
    private lateinit var placeListAdapter : PlaceListAdapter

    // private lateinit var placeNames : String -> 선택한 장소 이름
    private lateinit var timebtn: ImageButton
    private lateinit var placebtn: ImageButton
    private lateinit var placeLayout: ConstraintLayout
    private lateinit var placeListView: ListView
    private lateinit var savebtn: Button
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
    private var todoId = ""
    val interval = AlarmManager.INTERVAL_DAY
    private var notifyTime = false
    val date: LocalDateTime = LocalDateTime.now()
    private val TimeNotificationID = (System.currentTimeMillis()/1000).toInt()
    var hour = 0
    var amPm = "오전"
    var min = 0
    private lateinit var data : TodoData
    private var groupCheck = false
    private var hasData = false
    private var originGroupId = ""

    private var myReceiver: MyReceiver? = null
    private var mService: LocationUpdatesService? = null
    private var mBound = false

    private var placeData: PlaceData? = null
    private var placeList = arrayListOf<PlaceData>()

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationUpdatesService.LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        myReceiver = MyReceiver()

        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }
        // Toolbar달기
        todoToolBar = findViewById(R.id.settings_toolbar)
        todoToolBar.title = resources.getString(R.string.todo)
        setSupportActionBar(todoToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // presenter 초기화
        presenter = AddAddTodoPresenter(this)
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

        var timeAgainAdapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.again_time, android.R.layout.simple_spinner_dropdown_item
        )
        var placeAgainAdapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.again_time, android.R.layout.simple_spinner_dropdown_item
        )

        timebtn = viewTime.findViewById(R.id.btn_time_settings) //시간설정버튼
        timebtn.isClickable = false

        placebtn = viewLocation.findViewById(R.id.btn_place_choice) //장소선택 버튼
        placeListView = viewLocation.findViewById(R.id.listview_place) //장소선택시 나오는 listview

        timeDateLayout.setOnClickListener { //시간 날짜 설정
            var year = LocalDate.now().year
            var month = LocalDate.now().monthValue - 1
            var day = LocalDate.now().dayOfMonth
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                timeDateText.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                timeCalendar.set(Calendar.YEAR, year) // 년
                timeCalendar.set(Calendar.MONTH, month) // 월
                timeCalendar.set(Calendar.DATE, dayOfMonth) // 일
            }
            val dateDia = DatePickerDialog(this, dateListener, year, month, day)
            dateDia.show()
        }

        placeDateLayout.setOnClickListener { //장소 날짜 설정
            var year = LocalDate.now().year
            var month = LocalDate.now().monthValue - 1
            var day = LocalDate.now().dayOfMonth
            var dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                placeDateText.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                placeCalendar.set(Calendar.YEAR, year) //년
                placeCalendar.set(Calendar.MONTH, month) //월
                placeCalendar.set(Calendar.DATE, dayOfMonth) //일
            }
            val dateDia = DatePickerDialog(this, dateListener, year, month, day)
            dateDia.show()
        }

        //시간알림 반복시간 설정
        timeSpinner.adapter = timeAgainAdapter
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    settingsTimeMinutes = 0

                } else if(position == 1) {
                    settingsTimeMinutes = 5
                } else if( position == 2) {
                    settingsTimeMinutes = 10
                } else if( position == 3) {
                    settingsTimeMinutes = 15
                } else if( position == 4) {
                    settingsTimeMinutes = 30
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //장소 알림 반복시간 설정
        placeSpinner.adapter = placeAgainAdapter
        placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    settingsPlaceMinutes = 0
                } else if(position == 1) {
                    settingsPlaceMinutes = 5
                } else if( position == 2) {
                    settingsPlaceMinutes = 10
                } else if( position == 3) {
                    settingsPlaceMinutes = 15
                } else if( position == 4) {
                    settingsPlaceMinutes = 30
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        timeSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                notifyTime = true // 알람 켬.
                todoStubTime.visibility = VISIBLE
                timeCalendar.timeInMillis
                currentHour = timeCalendar.get(Calendar.HOUR_OF_DAY)
                currentMinute = timeCalendar.get(Calendar.MINUTE)
                if (timeCalendar.get(Calendar.HOUR_OF_DAY) < 12) {
                    amPm = "오전"
                } else {
                    amPm = "오후"
                    currentHour -= 12
                }
                timeText.text = "${amPm} ${currentHour} : ${String.format("%02d", currentMinute)}"
                timeLayout.setOnClickListener(timeDialogClickListener)
            } else {
                timeDateText.text = "[기본] 날짜 미설정"
                settingsTimeMinutes = 0
                todoStubTime.visibility = GONE
                notifyTime = false //알람 끔.
            }
        }

        placeSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                todoStubLocation.visibility = VISIBLE
                placeLayout.setOnClickListener(View.OnClickListener {
                    val placechoiceIntent =
                        Intent(it.context, PlaceAlarmDetailActivity::class.java)
                    placechoiceIntent.putExtra("placeData", placeData)
                    placechoiceIntent.putExtra("todoPlaceAlarm", placeList)
                    startActivityForResult(placechoiceIntent, 200)
                })
                notifyTime = true // 알람 켬.
            } else {
                placeDateText.text = "[기본] 날짜 미설정"
                settingsPlaceMinutes = 0
                this.placeList?.clear()
                todoStubLocation.visibility = GONE
                notifyTime = false //알람 끔.
            }
        }

        todoAlarm()
        receiverData()

        setPlaceListAdapter()

//        this.placeList = placeListAdapter.getList() // delete 후 값 가져오기

        if (intent.hasExtra("todoData")) {  //intent값을 가지고 있을때
            data = intent.getParcelableExtra("todoData")
            when (data.timeAgain) {
                0 -> timePosition = 0
                5 -> timePosition = 1
                10 -> timePosition = 2
                15 -> timePosition = 3
                30 -> timePosition = 4
            }
            when (data.placeAgain) {
                0 -> placePosition = 0
                5 -> placePosition = 1
                10 -> placePosition = 2
                15 -> placePosition = 3
                30 -> placePosition = 4
            }

            todoId = data.todoId
            groupId = data.groupId
            titleEdit.setText(data.title)
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
        }
        savebtn.setOnClickListener {
            when {
                groupName == "" -> {
                    Toast.makeText(applicationContext, "그룹을 선택해주세요", Toast.LENGTH_SHORT).show()
                }
                titleEdit.text.isEmpty() -> {
                    Toast.makeText(applicationContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if (groupCheck){
                        if(hasData) {
                            //presenter.deleteMemoInfo(originGroupId,memoId)
                        }
                        originGroupId = groupId
                    }
                    var todoData = TodoData(
                        todoId,
                        titleEdit.text.toString(),
                        groupId,
                        timeSwitch.isChecked,
                        "${timeDateText.text}",
                        timeText.text.toString(),
                        settingsTimeMinutes,
                        placeSwitch.isChecked,
                        "${placeDateText.text}",
                        settingsPlaceMinutes
                    )
                    presenter.addTodo(todoData, placeList)
                    if (timeSwitch.isChecked) {
                        // 지정한 시간에 울리게 알람을 세팅
                        setTimeAlarm(notifyTime, timeCalendar, settingsTimeMinutes)
                    } else {
                        unsetTimeAlarm() //시간알람 해제
                    }

                    //if (placeSwitch.isChecked) {
                    //      placeCalendar.set(Calendar.MINUTE, Calendar.MINUTE+settingsPlaceMinutes)
                    //      setTimeLocationAlarm(placeCalendar, settingsPlaceMinutes)
                    //}

                    finish()
                }
            }
        }

        selectGroupBtn.setOnClickListener {
            selectGroup()
        }

        if (intent.getStringExtra("mode") == "longPressed") {
            placeSwitch.isChecked = true
            todoStubLocation.visibility = VISIBLE
            placeData = intent.getParcelableExtra<PlaceData>("placeData")
            placeList = intent.getParcelableArrayListExtra("todoPlaceAlarm")
            setPlaceListAdapter()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                200 -> {
                    placeData = data!!.getParcelableExtra<PlaceData>("placeData")
                    placeList = data!!.getParcelableArrayListExtra("todoPlaceAlarm")
                    setPlaceListAdapter()
                }
            }
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
            } else {
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

        val dialog = TimePickerDialog(this, listener, 12, 0, false)
        dialog.show()
    }

    fun selectGroup() {
        var i = 0
        val items: Array<CharSequence> = Array(FolderObject.folderInfo.size) { "" }
        val groupIds: Array<CharSequence> = Array(FolderObject.folderInfo.size) { "" }

        FolderObject.folderInfo.forEach {
            groupIds[i] = it.key
            items[i] = it.value
            i++
        }
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
        listDialog.setTitle("그룹 선택")
            .setItems(items, DialogInterface.OnClickListener() { _, which ->
                selectGroupBtn.text = items[which]
                groupName = items[which].toString()
                groupId = groupIds[which].toString()
                groupCheck = true
            })
            .show()
    }

    fun setPlaceListAdapter() {
        placeListAdapter = PlaceListAdapter(this, placeList)
        placeListView.adapter = placeListAdapter
        presenter.setTodoPlaceAdapterModel(placeListAdapter)
        presenter.setTodoPlaceAdapterView(placeListAdapter)
    }

    private fun receiverData() {  //브로드캐스트에서 intent넘겨받기
        if (intent.hasExtra(BROADCAST)) {
            finish()
            var cancel = intent.getBooleanExtra(BROADCAST, false)
            Log.v("seyuuuun", "repeat: " + cancel.toString())
          
            if(cancel.equals(true)) {
                notifyTime = false
                timeSwitch.isChecked = false
                unsetTimeAlarm()
            }
        }
    }

    private fun todoAlarm() {
        val todoTime = UserObject.kakao_alarm_time
        if (!todoTime.equals("")) {
            var Todo = todoTime.split(" ")
            when (Todo.get(0)) { //오전 오후 구분
                "오후" ->
                    todoHour = Todo.get(1).toInt() + 12
                "오전" ->
                    todoHour = Todo.get(1).toInt()
            }
            todoMinute = Todo.get(3).toInt() //분

            todoCalendar.set(Calendar.HOUR_OF_DAY, todoHour)
            todoCalendar.set(Calendar.MINUTE, todoMinute)
            todoCalendar.set(Calendar.SECOND, 0)
            val currentTime = System.currentTimeMillis()
            var settingTime = todoCalendar.timeInMillis
            if (currentTime > settingTime) {
                todoCalendar.timeInMillis += interval //지정시간이 지난 경우 interval을 추가해줌.
            }
            setTodoAlarm(todoCalendar)
        } else {
            unsetTodoAlarm()
        }
    }

    private fun setTimeAlarm(notifyTime : Boolean, calendar: Calendar, settingTime: Int) {  //시간알람, 장소알람

        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootAlarmReceiver::class.java)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)

        if (titleEdit.text!=null) {
            val todoTitle = titleEdit.text.toString()
            alarmIntent.putExtra("todoTitle", todoTitle)
        }

        //DB작업끝난후 바꿔야함.
        alarmIntent.putExtra("todoPlace", "온누리 약국")

        alarmIntent.putExtra("todoId", TimeNotificationID) //reqeustcode 때문에 넣어준 것!!
        Log.v("seyuuuun", "notificationId " + TimeNotificationID)

        val pendingIntent = PendingIntent.getBroadcast(this, TimeNotificationID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val interval = 1000*60*settingTime


        if (notifyTime) { //알람을 허용했다면
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
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        }
    }

    private fun unsetTimeAlarm() {
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootAlarmReceiver::class.java)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, TimeNotificationID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(PendingIntent.getBroadcast(this, TimeNotificationID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)!=null && alarmManager!=null) {
            alarmManager.cancel(pendingIntent)
        }

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        Log.v("seyuuuun", "알림해제2 in time")
    }

    private fun setTodoAlarm(calendar: Calendar) {  // TODO 알람
        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootTodoReceiver::class.java)
        val alarmIntent = Intent(this, TodoReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            todoalarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )  //Broadcast Receiver시작
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (PendingIntent.getBroadcast(
                this,
                1,
                todoalarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            ) != null && alarmManager != null
        ) { //알림 해제
            alarmManager.cancel(pendingIntent)
        }
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    companion object {
        private const val PACKAGE_NAME = "com.kakao.smartmemo"
        val BROADCAST = "$PACKAGE_NAME.broadcast"
        val NOTIFICATION = "$PACKAGE_NAME.notifiation"
        val NOTIFICATIONID = (System.currentTimeMillis()/1000).toInt()
        private val TAG = AddTodo::class.java.simpleName
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        //receiverData()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            mService?.requestLocationUpdates()
        }
        //mService!!.removeLocationUpdates()  과부하 방지를 위해 남겨놓음.일단은!!!!

        bindService(
            Intent(this, LocationUpdatesService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver!!,
            IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection)
            mBound = false
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(
                TAG,
                "Displaying permission rationale to provide additional context."
            )
            Snackbar.make(
                findViewById(R.id.time_location_settings_layout),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) { // Request permission
                    ActivityCompat.requestPermissions(
                        this@AddTodo,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                this@AddTodo,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService?.requestLocationUpdates()
                Log.v("seyuuuun", "reqeustLocationUpdates in onRequestPermissions")
            } else {
                // Permission denied.
                //setButtonsState(false)   !!!
                Snackbar.make(
                    findViewById(R.id.time_location_settings_layout),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) { // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            "com.kakao.smartmemo", null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }

    /**
     * Receiver for broadcasts sent by [LocationUpdatesService].
     */
    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location =
                intent.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION)
            if (location != null) {
                Toast.makeText(
                    this@AddTodo, Utils.getLocationText(location),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences,
        s: String
    ) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s == Utils.KEY_REQUESTING_LOCATION_UPDATES) {
            /*setButtonsState(
                sharedPreferences.getBoolean(
                    Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false
                )  //->True
            )*/
        }
    }

    override fun onSuccess(placeList: MutableList<PlaceData>) {
        // Service로 위치 정보 넘기기 placeList가 PlaceData 형식이 들어있는 리스트에용
        Log.e("Add Todo placeList", placeList.toString())
    }

    override fun onAddSuccess() {
        presenter.getPlace("addTodo")
    }
}