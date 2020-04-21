package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MemoAdapterContract
import com.kakao.smartmemo.Contract.MemoContract

class MemoPresenter : MemoContract.Presenter {
    var view: MemoContract.View
    private lateinit var adapterView : MemoAdapterContract.View
    private lateinit var adapterModel : MemoAdapterContract.Model

    constructor(view: MemoContract.View) {
        this.view = view
    }

    override fun getGroup() {
    }

    override fun setMemoAdapterModel(model: MemoAdapterContract.Model) {
        adapterModel = model
    }

    override fun setMemoAdapterView(view: MemoAdapterContract.View) {
        adapterView = view
    }
}