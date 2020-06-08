package com.kakao.smartmemo.Model


import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.Object.UserObject


class GroupModel {
    private lateinit var onGetGroupInfoListener: MainContract.onGetGroupInfoListener
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseUser = FirebaseDatabase.getInstance().reference.child("User")
    private var firebaseGroup = FirebaseDatabase.getInstance().reference.child("Group")

    constructor()

    constructor(onGetGroupInfoListener: MainContract.onGetGroupInfoListener) {
        this.onGetGroupInfoListener = onGetGroupInfoListener
    }

    fun addGroup(groupName: String, color: Int) {
        //그룹 관련된 DB작업
        var groupId = (System.currentTimeMillis()*10000).toInt().toString()
        firebaseUser.child(UserObject.uid).child("GroupInfo")
            .updateChildren(mapOf(groupId to groupName))
        firebaseGroup.child(groupId).child("MemberInfo")
            .setValue(mapOf(UserObject.uid to UserObject.email))
        firebaseGroup.child(groupId).updateChildren(mapOf("group_color" to color))
        firebaseGroup.child(groupId).updateChildren(mapOf("group_name" to groupName))
        GroupObject.groupInfo[groupId] = groupName
        GroupObject.groupColor[groupId] = color.toLong()
    }

    fun updateGroup(groupId:String, groupName: String, color: Long?) {
        firebaseUser.child(UserObject.uid).child("GroupInfo").child(groupId).setValue(groupName)
        firebaseGroup.child(groupId).updateChildren(mapOf("group_name" to groupName))
        firebaseGroup.child(groupId).updateChildren(mapOf("group_color" to color))
        GroupObject.groupInfo[groupId] = groupName
        if (color != null) {
            GroupObject.groupColor[groupId] = color.toLong()
        }
    }

    fun getGroupInfo() {
        var i = 0
        var groupInfoList: HashMap<String, Long> = HashMap()
        var groupIdList = mutableListOf<String>()
        Log.e("groupName", GroupObject.groupInfo.size.toString())
        GroupObject.groupInfo.forEach {
            firebaseGroup.child(it.key).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(snapShot: DataSnapshot) {
                    GroupObject.groupColor[it.key] =
                        snapShot.child("group_color").value.toString().toLong()
                    groupIdList.add(it.key)
                    if(i == GroupObject.groupInfo.size-1){
                        onGetGroupInfoListener.onSuccess(groupIdList)
                    }
                    i++
                }
            })
        }
    }

    fun deleteGroup(groupId: String) {
        firebaseUser.child(UserObject.uid).child("GroupInfo").child(groupId).removeValue()
        firebaseGroup.child(groupId).child("MemberInfo").child(UserObject.uid).removeValue()
        GroupObject.groupInfo.remove(groupId)
        GroupObject.groupColor.remove(groupId)
    }


}