package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AddMemoContract

class AddMemoPresenter: AddMemoContract.Presenter {
    private var view: AddMemoContract.View

    constructor(view: AddMemoContract.View) {
        this.view = view
    }

    override fun addMemo() {

    }
}