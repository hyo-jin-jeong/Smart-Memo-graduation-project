package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.MemoData
import java.text.FieldPosition


interface MemoContract {
     interface View  {
         fun showMemoItem(position: Int)
         fun showAllMemo(memoList: MutableList<MemoData>)
     }

    interface  Presenter{
        fun getMemo()
        fun getGroup()
        fun setMemoAdapterModel(model: MemoAdapterContract.Model)
        fun setMemoAdapterView(view: MemoAdapterContract.View)
        fun setMemoDeleteAdapterModel(deleteAdapterModel : MemoDeleteAdapterContract.Model)
        fun setMemoDeleteAdapterView(deleteAdapterView : MemoDeleteAdapterContract.View)

    }
    interface OnMemoListener{
        fun onSuccess(memoList: MutableList<MemoData>)
        fun onFailer()
    }
}