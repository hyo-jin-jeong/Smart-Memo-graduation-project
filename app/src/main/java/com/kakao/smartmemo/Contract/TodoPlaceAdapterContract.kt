package com.kakao.smartmemo.Contract

interface TodoPlaceAdapterContract {

    interface Model {
        fun getTodoPlace()
        fun deletePlace()
    }

    interface View {
        fun notifyAdapter()
    }
}