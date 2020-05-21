package com.kakao.smartmemo.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.Object.UserObject

class GroupModel {
    private lateinit var onGetGroupInfoListener : MainContract.onGetGroupInfoListener
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var groupAddToUser = firestore.collection("User")
    private var groupAddToGroup = firestore.collection("Group")

    constructor()

    constructor(onGetGroupInfoListener: MainContract.onGetGroupInfoListener) {
        this.onGetGroupInfoListener = onGetGroupInfoListener
    }
    fun addGroup(groupName: String, color: Int) {
        //그룹 관련된 DB작업
        groupAddToUser.document("${UserObject.email}").collection("GroupInfo").document("GroupId").set(
            hashMapOf(UserObject.email+groupName to UserObject.email+groupName),
            SetOptions.merge())

        groupAddToGroup.document(UserObject.email + groupName).set(hashMapOf("group_name" to groupName,"group_color" to color))
        groupAddToGroup.document(UserObject.email + groupName).collection("MemberInfo").document("MemberEmail").set(
            hashMapOf(UserObject.email to UserObject.email), SetOptions.merge())
        GroupObject.groupInfo.add(UserObject.email+groupName)
    }
    fun updateGroup() {

    }

    fun getGroupInfo() {
        var groupInfoList : HashMap<String, Long> = HashMap()
        groupAddToGroup.addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.forEach {
                for(i in 0 until GroupObject.groupInfo.size){
                    if(it.id == GroupObject.groupInfo[i]){
                        groupInfoList[it.get("group_name") as String] = it.get("group_color") as Long
                    }
                }
            }
            onGetGroupInfoListener.onSuccess(groupInfoList)
        }
    }


}