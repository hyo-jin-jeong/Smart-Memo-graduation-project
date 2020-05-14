package com.kakao.smartmemo.Contract

interface MainContract {
    interface Presenter {
        fun setMainAdapterModel(model: MainAdapterContract.Model)
        fun setMainAdapterView(view: MainAdapterContract.View)
        fun getGroupData()
    }
    interface View {
        fun setNavigationView(name: MutableList<String>)


    }

    interface OnGetDataSuccessListener {
        fun onSuccess(name: MutableList<String>)
        fun onFailure()
    }

}