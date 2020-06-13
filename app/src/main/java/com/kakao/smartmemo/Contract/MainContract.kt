package com.kakao.smartmemo.Contract

interface MainContract {
    interface Presenter {
        fun setMainAdapterModel(model: MainAdapterContract.Model)
        fun setMainAdapterView(view: MainAdapterContract.View)
        fun getGroupInfo()

    }
    interface View {
       fun onSuccess(action: MutableList<String>)
    }

    interface onGetGroupInfoListener {
        fun onSuccess(groupInfoList: MutableList<String>)
        fun onFailure()
    }

}