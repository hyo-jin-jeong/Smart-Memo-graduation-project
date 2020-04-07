package com.kakao.smartmemo

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.DTO.PlaceDTO
import kotlinx.android.synthetic.main.alarm_settings_time.*
import kotlinx.android.synthetic.main.time_location_settings.*

class TodoListActivity : AppCompatActivity() {

    private lateinit var myToolbar: Toolbar
    private lateinit var alarmswitch_time : Switch
    private lateinit var alarmswitch_location : Switch
    private lateinit var timebtn: ImageButton
    private lateinit var timeagainbtn : ImageButton
    private lateinit var placebtn: ImageButton
    private lateinit var placelistview : ListView
    private lateinit var placeagainbtn: ImageButton
    private var placeList = arrayListOf<PlaceDTO>(PlaceDTO("연세병원"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        // Toolbar달기
        myToolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(myToolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val todostub_time = stub_alarm_time
        val view_time = todostub_time.inflate()
        todostub_time.visibility = GONE

        val todostub_location = stub_alarm_location
        val view_location = todostub_location.inflate()
        todostub_location.visibility = GONE

        val textview_Time = textView_time_show

        alarmswitch_time = this.findViewById(R.id.switch_time)
        alarmswitch_location = this.findViewById(R.id.switch_location)
        timebtn = view_time.findViewById(R.id.btn_time_settings)
        placebtn = view_location.findViewById(R.id.btn_place_choice)
        placelistview = view_location.findViewById(R.id.listview_place)
        placeagainbtn = view_location.findViewById(R.id.btn_repeat_place)

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

            }else {
                todostub_time.visibility = GONE
                Log.v("seyuuuun", "viewstub_gone")
            }
        }

        alarmswitch_location.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todostub_location.visibility = VISIBLE
                placebtn.setOnClickListener(View.OnClickListener {
                    val placechoiceIntent = Intent(it.context, MainActivity::class.java)
                    this.startActivity(placechoiceIntent)
                })
                Log.v("seyuuuun", "viewstub")

            }else {
                todostub_location.visibility = GONE
                Log.v("seyuuuun", "viewstub_gone")
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