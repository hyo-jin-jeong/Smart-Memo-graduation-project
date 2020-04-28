package com.kakao.smartmemo.Contract


interface AddGroupContract {
    interface Presenter {
        fun addGroup(group_name: String, color: Int)
    }

    interface View {

    }
}