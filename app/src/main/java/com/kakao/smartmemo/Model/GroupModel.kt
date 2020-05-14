package com.kakao.smartmemo.Model


import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Object.UserObject

class GroupModel {
    private lateinit var onGetDataSuccessListener : MainContract.OnGetDataSuccessListener
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var groupAddToUser = firestore.collection("User")
    private var groupAddToGroup = firestore.collection("Group")

    constructor()

    constructor(onGetDataSuccessListener: MainContract.OnGetDataSuccessListener) {
        this.onGetDataSuccessListener = onGetDataSuccessListener
    }
    fun addGroup(groupName: String, color: Int) {
        //그룹 관련된 DB작업
        groupAddToUser.document("${UserObject.email}").collection("GroupId").document("$groupName").set(hashMapOf("group_id" to UserObject.email + groupName,"group_color" to color))
        groupAddToGroup.document(UserObject.email + groupName).set(hashMapOf("group_name" to groupName,"group_color" to color))
        groupAddToGroup.document(UserObject.email + groupName).collection("Member").document("MemberId").set(
            hashMapOf("email" to UserObject.email))
    }
    fun updateGroup() {

    }

    fun getGroupData(){
        var name : MutableList<String> = mutableListOf()
        var i =0
        groupAddToUser.document("${UserObject.email}").collection("GroupId").addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.forEach {
                name.add(i,it.id)
                i++
            }
            onGetDataSuccessListener.onSuccess(name)
        }

    }
}