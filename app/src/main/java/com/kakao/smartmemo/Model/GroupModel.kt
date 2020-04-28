package com.kakao.smartmemo.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.smartmemo.Object.UserObject

class GroupModel {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var groupAddToUser = firestore.collection("User").document(UserObject.email).collection("GroupId")
    var groupAddToGroup = firestore.collection("Group")
    fun addGroup(groupName: String, color: Int) {
        //그룹 관련된 DB작업
        groupAddToUser.document(groupName).set(hashMapOf("group_id" to UserObject.email + groupName,"group_color" to color))
        groupAddToGroup.document(UserObject.email + groupName).set(hashMapOf("group_name" to groupName,"group_color" to color))
        groupAddToGroup.document(UserObject.email + groupName).collection("Member").document(UserObject.email)
    }
    fun updateGroup() {

    }
}