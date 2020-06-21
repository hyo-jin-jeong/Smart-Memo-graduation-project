package com.kakao.smartmemo.Contract

interface AddFolderContract {
    interface Presenter {
        fun addGroup(groupId:String,group_name: String, color: Int)
    }

    interface View {

    }
}