package com.kakao.smartmemo.Contract

interface ShowMemoContract {
    interface View {

    }
    interface Presenter{
        fun getMemo()
        fun updateMemo()
        fun deleteMemo()
    }
}