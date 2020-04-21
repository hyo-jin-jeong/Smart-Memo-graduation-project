package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MapContract

class MapPresenter: MapContract.Presenter {
    private var view: MapContract.View

    constructor(view: MapContract.View) {
        this.view = view
    }

    override fun getMemo() {
    }

    override fun getTodoPlaceAlarm() {
    }
}