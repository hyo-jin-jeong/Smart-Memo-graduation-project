package com.kakao.smartmemo.Contract

interface MemoContract {
     interface View {

     }

    interface  Presenter{
        fun getGroup()
        fun setMemoAdapterModel(model: MemoAdapterContract.Model)
        fun setMemoAdapterView(view: MemoAdapterContract.View)
        fun setMemoDeleteAdapterModel(deleteAdapterModel : MemoDeleteAdapterContract.Model)
        fun setMemoDeleteAdapterView(deleteAdapterView : MemoDeleteAdapterContract.View)
    }
}