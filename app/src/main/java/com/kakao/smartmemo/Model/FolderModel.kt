package com.kakao.smartmemo.Model


import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Object.UserObject


class FolderModel {
    private lateinit var onGetGroupInfoListener: MainContract.onGetGroupInfoListener
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

        with(firebaseGroup.child(groupId)) {
            child("MemberInfo").setValue(mapOf(UserObject.uid to UserObject.email))
            updateChildren(mapOf("group_color" to color))
            updateChildren(mapOf("group_name" to groupName))
        }
        FolderObject.folderInfo[groupId] = groupName
        FolderObject.folderColor[groupId] = color.toLong()

    }

    fun updateGroup(groupId:String, groupName: String, color: Long?) {
        firebaseUser.child(UserObject.uid).child("GroupInfo").child(groupId).setValue(groupName)

        with(firebaseGroup.child(groupId)) {
            updateChildren(mapOf("group_name" to groupName))
            updateChildren(mapOf("group_color" to color))
        }
        FolderObject.folderInfo[groupId] = groupName
        if (color != null) {
            FolderObject.folderColor[groupId] = color.toLong()
        }

    }

    fun getGroupInfo() {
        var i = 0
        var groupInfoList: HashMap<String, Long> = HashMap()
        var groupIdList = mutableListOf<String>()
        Log.e("groupName", FolderObject.folderInfo.size.toString())
        FolderObject.folderInfo.forEach {
            firebaseGroup.child(it.key).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(snapShot: DataSnapshot) {
                    FolderObject.folderColor[it.key] =
                        snapShot.child("group_color").value.toString().toLong()
                    groupIdList.add(it.key)
                    if(i == FolderObject.folderInfo.size-1){
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
        FolderObject.folderInfo.remove(groupId)
        FolderObject.folderColor.remove(groupId)
    }


}