package com.kakao.smartmemo.Model

import com.kakao.smartmemo.Data.TimeAlarmData

class TimeAlarmModel {


    fun addTimeAlarm() {

    }
    fun removeTimeAlarm(position: Int) : TimeAlarmData {
        return TimeAlarmData("", "", "", false)
    }
    fun getTimeAlarm(position: Int) : TimeAlarmData {
        return TimeAlarmData("", "", "", false)
    }
}
