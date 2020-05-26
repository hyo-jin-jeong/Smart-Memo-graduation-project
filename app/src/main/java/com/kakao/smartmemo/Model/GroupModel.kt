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
        var groupId = UserObject.email + System.currentTimeMillis()
        groupAddToUser.document("${UserObject.email}").collection("GroupInfo").document("GroupId").set(
            hashMapOf(groupId to groupName),
            SetOptions.merge())

        groupAddToGroup.document("$groupId").set(hashMapOf("group_name" to groupName,"group_color" to color))
        groupAddToGroup.document("$groupId").collection("MemberInfo").document("MemberEmail").set(
            hashMapOf(UserObject.email to UserObject.email), SetOptions.merge())
        GroupObject.groupInfo[groupId] = groupName
    }
    fun updateGroup() {

    }

    fun getGroupInfo() {
        var groupInfoList : HashMap<String, Long> = HashMap()

        groupAddToGroup.addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.forEach {snapShot ->
                GroupObject.groupInfo.forEach {
                    if(snapShot.id == it.key){
                        groupInfoList[snapShot.get("group_name") as String] = snapShot.get("group_color") as Long

                    }

                }


            }
            onGetGroupInfoListener.onSuccess(groupInfoList)
        }
    }


}