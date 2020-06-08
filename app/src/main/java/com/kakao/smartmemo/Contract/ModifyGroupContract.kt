package com.kakao.smartmemo.Contract

interface ModifyGroupContract {
    interface Presenter {
        fun updateGroup(groupId: String, groupName: String, groupColor: Long)
        fun deleteGroup(groupId: String)
    }

    interface View {

    }

}