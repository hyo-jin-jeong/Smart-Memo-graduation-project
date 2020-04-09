package com.kakao.smartmemo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import com.kakao.smartmemo.DTO.PlaceDTO
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.DayRepeatAdapter
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.PlaceListAdapter
import com.kakao.smartmemo.com.kakao.smartmemo.DTO.DayDTO
import kotlinx.android.synthetic.main.alarm_settings_place.*
import kotlinx.android.synthetic.main.alarm_settings_time.*
import kotlinx.android.synthetic.main.time_location_settings.*
import java.time.LocalDate

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

    private var placeList = arrayListOf<PlaceDTO>(PlaceDTO("연세병원"))
    private var dayList = mutableListOf<DayDTO>(DayDTO("월"), DayDTO("화"), DayDTO("수"), DayDTO("목"), DayDTO("금"), DayDTO("토"), DayDTO("일"))

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
}