package com.kakao.smartmemo.Contract

interface TodoDialogAdapterContract {
    interface Model {
        fun getTodoPlaceAlarm()
    }

    interface View {
        fun notifyAdapter()
    }

}
