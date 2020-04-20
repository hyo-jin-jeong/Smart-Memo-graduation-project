package com.kakao.smartmemo.Contract

interface AlarmContract {
    interface View {

    }

    interface Presenter {
        fun setAlarmAdapterModel(adapterModel : AlarmAdapterContract.Model)
        fun setAlarmAdapterView(adapterView : AlarmAdapterContract.View)

    }
}