package com.kakao.smartmemo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
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
    private lateinit var alarmswitch : Switch
    private lateinit var alarmlayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)

        val todostub = stub_alarm
        val todoview = todostub.inflate()

        alarmswitch = this.findViewById(R.id.switch_time)

        if(alarmswitch.isChecked) {
            todoview.visibility = VISIBLE
        }else {
            todoview.visibility = GONE
        }
    }
}