package com.kakao.smartmemo

import android.app.TimePickerDialog
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.member_change_view.*

class MemberDataChange :AppCompatActivity(){
    lateinit var memberToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.member_change_view)
        memberToolbar = findViewById(R.id.member_toolbar)
        setSupportActionBar(memberToolbar)
        member_icon.setClipToOutline(true)

        //앱 이름 없애는-
        getSupportActionBar()?.setDisplayShowTitleEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        text7.setOnClickListener {
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
                time_text.text = "${am_pm} ${hour} : ${m} "
            }
            val dialog = TimePickerDialog(this,listener,12,0,false)

            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> super.onOptionsItemSelected(item)
            }
        }

}