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
import java.text.SimpleDateFormat
import java.util.*

class TodoListActivity : AppCompatActivity() {
    private lateinit var myToolbar: Toolbar
    private lateinit var alarmswitch_time : Switch
    private lateinit var alarmswitch_location : Switch
    private lateinit var timebtn: ImageButton
    private lateinit var placebtn: ImageButton
    private lateinit var placelistview : ListView

    private var placeList = arrayListOf<PlaceDTO>(PlaceDTO("연세병원"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        // Toolbar달기
        myToolbar = findViewById(R.id.settingstoolbar)
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

        alarmswitch_time.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todostub_time.visibility = VISIBLE
                timebtn.setOnClickListener(View.OnClickListener {
                    val cal = Calendar.getInstance()
                    val am_pm = ""
                    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)

                        textview_Time.text =SimpleDateFormat("a HH:mm").format(cal.time)
                    }
                    TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
                })
                Log.v("seyuuuun", "viewstub")
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