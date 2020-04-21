package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.ShowMemoContract

class ShowMemoPresenter: ShowMemoContract.Presenter {
    private var view: ShowMemoContract.View

    constructor(view: ShowMemoContract.View) {
        this.view = view
    }

    override fun getMemo() {

    }

    override fun updateMemo() {

    }

    override fun deleteMemo() {

    }
}