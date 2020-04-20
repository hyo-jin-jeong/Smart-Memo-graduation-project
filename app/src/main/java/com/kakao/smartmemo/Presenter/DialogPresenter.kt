package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.DialogAdapterContract
import com.kakao.smartmemo.Contract.DialogContract

class DialogPresenter : DialogContract.Presenter{
    var view : DialogContract.View
    private lateinit var adapterView : DialogAdapterContract.View
    private lateinit var adapterModel : DialogAdapterContract.Model

    constructor(view : DialogContract.View) {
        this.view = view
    }
    override fun setDialogAdapterModel(adapterModel: DialogAdapterContract.Model) {
        this.adapterModel = adapterModel
    }

    override fun setDialogAdapterView(adapterView: DialogAdapterContract.View) {
        this.adapterView = adapterView
    }
}