package com.kakao.smartmemo.Contract

interface MainContract {
    interface Presenter {
        fun setMainAdapterModel(model: MainAdapterContract.Model)
        fun setMainAdapterView(view: MainAdapterContract.View)
    }
    interface View {

    }

}