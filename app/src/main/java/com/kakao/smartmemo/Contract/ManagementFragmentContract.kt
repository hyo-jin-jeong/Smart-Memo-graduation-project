package com.kakao.smartmemo.Contract

interface ManagementFragmentContract {
    interface View{


    }

    interface  Presenter{
        fun setManagementAdapterView(adapter: ManagementAdapterContract.View)
        fun setManagementAdapterModel(adapter: ManagementAdapterContract.Model)
    }
}