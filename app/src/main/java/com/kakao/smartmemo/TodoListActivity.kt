package com.kakao.smartmemo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.kakao.smartmemo.DTO.PlaceDTO
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
    private lateinit var timeSpinner : Spinner
    private lateinit var placeSpinner : Spinner

    private var placeList = arrayListOf<PlaceDTO>(PlaceDTO("연세병원"))

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
        timebtn = view_time.findViewById(R.id.btn_time_settings)

        placebtn = view_location.findViewById(R.id.btn_place_choice)
        placelistview = view_location.findViewById(R.id.listview_place)

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


        timeSpinner = repeat_time_spinner
        timeSpinner.adapter = ringingAdapter
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {   }
        }

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

        placelistview.adapter = PlaceListAdapter(this, placeList)
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
}