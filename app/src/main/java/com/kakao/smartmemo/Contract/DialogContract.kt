package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Data.PlaceAlarmData
import com.kakao.smartmemo.Data.PlaceData


interface DialogContract {
    interface View {
        fun onSuccessMemo(memoList: MutableList<MemoData>)
        fun onSuccessTodo(todoList:MutableList<PlaceAlarmData>)
    }
    interface Presenter {
        fun setDialogAdapterModel(adapterModel: DialogAdapterContract.Model)
        fun setDialogAdapterView(adapterView: DialogAdapterContract.View)
        fun getMemoList(memo: MutableList<PlaceData>)
        fun getTodoList(todo: MutableList<PlaceData>)
    }

    interface OnDialogListener {
        fun onSuccessMemo(memoList: MutableList<MemoData>)
        fun onSuccessTodo(todoList:MutableList<PlaceAlarmData>)
    }
}