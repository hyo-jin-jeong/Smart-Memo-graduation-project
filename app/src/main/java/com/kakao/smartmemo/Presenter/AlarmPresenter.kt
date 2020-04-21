package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AlarmAdapterContract
import com.kakao.smartmemo.Contract.AlarmContract
import com.kakao.smartmemo.Model.PlaceAlarmModel
import com.kakao.smartmemo.Model.TimeAlarmModel

class AlarmPresenter : AlarmContract.Presenter{

    private var view : AlarmContract.View
    private var placeAlarmModel : PlaceAlarmModel
    private var timeAlarmModel : TimeAlarmModel

    private lateinit var alarmAdapterView : AlarmAdapterContract.View
    private lateinit var alarmAdapterModel : AlarmAdapterContract.Model

    constructor(view : AlarmContract.View) {
        this.view = view
        this.placeAlarmModel = PlaceAlarmModel()
        this.timeAlarmModel = TimeAlarmModel()
    }

    override fun setAlarmAdapterModel(adapterModel: AlarmAdapterContract.Model) {
        alarmAdapterModel = adapterModel
    }

    override fun setAlarmAdapterView(adapterView: AlarmAdapterContract.View) {
        alarmAdapterView  = adapterView
    }


}