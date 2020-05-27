package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AddMemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Model.MemoModel

class AddMemoPresenter: AddMemoContract.Presenter {
    private var view: AddMemoContract.View
    private var model : MemoModel

    constructor(view: AddMemoContract.View) {
        this.view = view
        this.model = MemoModel()
    }

    override fun addMemo(memoData: MemoData) {
        model.addMemo(memoData)
    }
}