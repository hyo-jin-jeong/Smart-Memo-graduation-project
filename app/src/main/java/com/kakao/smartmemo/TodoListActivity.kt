package com.kakao.smartmemo

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity


class TodoListActivity : AppCompatActivity() {
    private lateinit var alarmswitch : Switch
    private lateinit var alarmlayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_location_settings)


    }
}