package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.MemoData

interface ShowMemoContract {
    interface View {

    }
    interface Presenter{
        fun getMemo()
        fun updateMemo()
        fun deleteMemo(memoData: MemoData)
    }
}