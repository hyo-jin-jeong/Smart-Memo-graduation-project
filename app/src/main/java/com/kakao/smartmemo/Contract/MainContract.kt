package com.kakao.smartmemo.Contract

interface MainContract {
    interface Presenter {
        fun setMainAdapterModel(model: MainAdapterContract.Model)
        fun setMainAdapterView(view: MainAdapterContract.View)
        fun getGroupInfo()

    }
    interface View {
        fun setNavigationView(groupInfoList: HashMap<String, Long>)


    }

    interface onGetGroupInfoListener {
        fun onSuccess(groupInfoList: HashMap<String, Long>)
        fun onFailure()
    }

}