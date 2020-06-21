package com.kakao.smartmemo.Contract

interface MainContract {
    interface Presenter {
        fun setMainAdapterModel(model: MainAdapterContract.Model)
        fun setMainAdapterView(view: MainAdapterContract.View)
        fun getGroupInfo()
        fun checkFolderMember(groupId: String?, groupName: String?)
    }

    interface View {
       fun onSuccess()
    }

    interface OnMainListener {
        fun onSuccess()
        fun onFailure()
    }

}