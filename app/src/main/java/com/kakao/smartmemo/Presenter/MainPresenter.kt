package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MainContract

class MainPresenter : MainContract.Presenter{

    private var view : MainContract.View

    constructor(view: MainContract.View){
        this.view = view
    }

}