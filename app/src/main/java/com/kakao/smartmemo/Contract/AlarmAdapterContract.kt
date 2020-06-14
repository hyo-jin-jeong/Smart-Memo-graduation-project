package com.kakao.smartmemo.Contract

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