package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MainAdapterContract
import com.kakao.smartmemo.Contract.MainContract

class MainPresenter : MainContract.Presenter{

    private var view : MainContract.View
    private lateinit var adapterView: MainAdapterContract.View
    private lateinit var adapterModel: MainAdapterContract.Model

    constructor(view: MainContract.View){
        this.view = view
    }

    override fun setMainAdapterModel(model: MainAdapterContract.Model) {
        adapterModel = model
    }

    override fun setMainAdapterView(view: MainAdapterContract.View) {
        adapterView = view
    }

}