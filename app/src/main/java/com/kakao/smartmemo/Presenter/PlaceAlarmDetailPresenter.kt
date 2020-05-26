package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.PlaceAlarmDetailContract

class PlaceAlarmDetailPresenter: PlaceAlarmDetailContract.Presenter {
    private var view : PlaceAlarmDetailContract.View

    constructor(view: PlaceAlarmDetailContract.View){
        this.view = view
    }
}