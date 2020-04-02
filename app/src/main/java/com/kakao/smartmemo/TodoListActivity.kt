package com.kakao.smartmemo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.time_location_settings.*
import java.util.zip.Inflater

class TodoListActivity : AppCompatActivity() {
    private lateinit var alarmswitch_time : Switch
    private lateinit var alarmswitch_location : Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        val todostub_time = stub_alarm_time
        val todostub_location = stub_alarm_location

        alarmswitch_time = this.findViewById(R.id.switch_time)
        alarmswitch_location = this.findViewById(R.id.switch_location)

        alarmswitch_time.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todostub_time.visibility = VISIBLE
                Log.v("seyuuuun", "viewstub")
            }else {
                todostub_time.visibility = GONE
                Log.v("seyuuuun", "viewstub_gone")
            }
        }

        alarmswitch_location.setOnCheckedChangeListener { compoundButton, isChecked->
            if(isChecked) {
                todostub_location.visibility = VISIBLE
                Log.v("seyuuuun", "viewstub")
            }else {
                todostub_location.visibility = GONE
                Log.v("seyuuuun", "viewstub_gone")
            }
        }

    }
}