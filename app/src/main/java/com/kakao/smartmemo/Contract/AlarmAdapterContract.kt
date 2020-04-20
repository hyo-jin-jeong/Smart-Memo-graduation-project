package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Model.PlaceAlarmModel
import com.kakao.smartmemo.Model.TimeAlarmModel

interface AlarmAdapterContract {

    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun setTimeAlarmModel(model : TimeAlarmModel)
        fun setPlaceAlarmModel(model: PlaceAlarmModel)
    }
}