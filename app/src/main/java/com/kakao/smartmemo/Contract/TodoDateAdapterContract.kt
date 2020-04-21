package com.kakao.smartmemo.Contract

interface TodoDateAdapterContract {
    interface Model {
        fun addRepeatDay()
    }

    interface View {
        fun notifyAdapter()
    }

}
