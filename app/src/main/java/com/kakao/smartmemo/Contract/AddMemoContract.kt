package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.MemoData

interface AddMemoContract {
    interface View {

    }
    interface Presenter{
        fun addMemo(memoData: MemoData)
        fun deleteMemoInfo(groupId:String,memoId:String)
    }
}