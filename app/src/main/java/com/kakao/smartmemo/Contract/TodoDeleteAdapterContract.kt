package com.kakao.smartmemo.Contract

interface TodoDeleteAdapterContract {
    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun getTodoContent()
        fun deleteTodo()
        fun getGroup()
    }
}
