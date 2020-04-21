package com.kakao.smartmemo.Contract

interface TodoAdapterContract {

    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun getTodoContent()
        //그룹의 색을 표시하기 위해
        fun getGroup()
    }
}