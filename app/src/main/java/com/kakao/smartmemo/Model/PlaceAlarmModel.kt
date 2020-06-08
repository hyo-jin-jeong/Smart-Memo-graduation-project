package com.kakao.smartmemo.Model

import com.kakao.smartmemo.Data.PlaceAlarmData

class PlaceAlarmModel {

    fun addPlaceAlarm() {

    }
    fun removePlaceAlarm(position: Int) : PlaceAlarmData {
        return PlaceAlarmData("", "", "", false)
    }
    fun getPlaceAlarm(position: Int) : PlaceAlarmData {
        return PlaceAlarmData("", "", "", false)
    }

}
