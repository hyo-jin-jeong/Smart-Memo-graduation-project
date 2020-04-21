package com.kakao.smartmemo.Contract

interface MainAdapterContract {
    interface Model {
        fun getUser()
        fun getGroup()
    }

    interface View {
        fun notifyAdapter()
    }

}
