package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Model.PlaceAlarmModel
import com.kakao.smartmemo.Model.TimeAlarmModel

interface AlarmAdapterContract {

    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun addTimeAlarm()
        fun addPlaceAlarm()
        fun updateTimeAlarm()
        fun updatePlaceAlarm()
        fun deleteTimeAlarm()
        fun deletePlaceAlarm()
    }
}