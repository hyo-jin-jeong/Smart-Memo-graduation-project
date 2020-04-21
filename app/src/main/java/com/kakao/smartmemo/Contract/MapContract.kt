package com.kakao.smartmemo.Contract

interface MapContract {
    interface View {

    }
    interface Presenter{
        fun getMemo()
        fun getTodoPlaceAlarm()
    }
}