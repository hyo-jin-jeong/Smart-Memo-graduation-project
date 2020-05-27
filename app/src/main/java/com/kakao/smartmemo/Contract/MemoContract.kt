package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.MemoData


interface MemoContract {
     interface View  {
        fun showMemoItem(memoData: MemoData)
     }

    interface  Presenter{
        fun getMemo()
        fun getGroup()
        fun setMemoAdapterModel(model: MemoAdapterContract.Model)
        fun setMemoAdapterView(view: MemoAdapterContract.View)
        fun setMemoDeleteAdapterModel(deleteAdapterModel : MemoDeleteAdapterContract.Model)
        fun setMemoDeleteAdapterView(deleteAdapterView : MemoDeleteAdapterContract.View)
    }
}