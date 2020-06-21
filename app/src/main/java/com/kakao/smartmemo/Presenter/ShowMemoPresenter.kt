package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.ShowMemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Model.MemoModel

class ShowMemoPresenter: ShowMemoContract.Presenter {
    private var view: ShowMemoContract.View
    private var model: MemoModel

    constructor(view: ShowMemoContract.View) {
        this.view = view
        this.model = MemoModel()
    }

    override fun deleteMemo(memoData: MemoData) {
        model.deleteOneMemo(memoData)
    }
}