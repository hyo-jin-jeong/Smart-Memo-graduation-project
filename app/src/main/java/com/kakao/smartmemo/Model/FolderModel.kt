package com.kakao.smartmemo.Model


import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Data.FolderData
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Object.UserObject


class FolderModel {
    private lateinit var onGetGroupInfoListener: MainContract.onGetGroupInfoListener
    private var firebaseUser = FirebaseDatabase.getInstance().reference.child("User")
    private var firebaseFolder = FirebaseDatabase.getInstance().reference.child("Group")

    constructor()

    constructor(onGetGroupInfoListener: MainContract.onGetGroupInfoListener) {
        this.onGetGroupInfoListener = onGetGroupInfoListener
    }

    fun addGroup(groupName: String, color: Int) {
        //그룹 관련된 DB작업
        var groupId = (System.currentTimeMillis()*10000).toInt().toString()
        var folderData = FolderData(groupName,color)
        firebaseUser.child(UserObject.uid).child("GroupInfo").updateChildren(mapOf(groupId to groupName))
        with(firebaseFolder.child(groupId)) {
            setValue(folderData)
            child("MemberInfo").setValue(mapOf(UserObject.uid to UserObject.email))
        }
        FolderObject.folderInfo[groupId] = groupName
        FolderObject.folderColor[groupId] = color.toLong()

    }

    fun updateGroup(groupId:String, groupName: String, color: Long?) {
        firebaseUser.child(UserObject.uid).child("GroupInfo").child(groupId).setValue(groupName)
        with(firebaseFolder.child(groupId)) {
            updateChildren(mapOf("folderName" to groupName))
            updateChildren(mapOf("folderColor" to color))
        }
        FolderObject.folderInfo[groupId] = groupName
        if (color != null) {
            FolderObject.folderColor[groupId] = color.toLong()
        }
    }

    fun getGroupInfo() {
        var i = 0
        var groupIdList = mutableListOf<String>()
        FolderObject.folderId.clear()
        Log.e("groupName", FolderObject.folderInfo.size.toString())
            firebaseUser.child(UserObject.uid).child("GroupInfo").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(folderSnapshot: DataSnapshot) {
                    folderSnapshot.children.forEach {
                        it.key?.let { it1 ->
                            FolderObject.folderInfo[it.key!!] = it.value.toString()
                            firebaseFolder.child(it1).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(snapShot: DataSnapshot) {
                                    Log.e("확인11","확인")
                                    FolderObject.folderShare[it.key!!] = snapShot.child("MemberInfo").children.count()>1
                                    FolderObject.folderColor[it.key!!] = snapShot.child("folderColor").value.toString().toLong()
                                    FolderObject.folderId.add(it.key!!)
                                    groupIdList.add(it.key!!)
                                    if(i == folderSnapshot.children.count()-1&&FolderObject.folderShare[it.key!!]!=null&&FolderObject.folderColor[it.key!!]!=null){
                                       // onGetGroupInfoListener.onSuccess(groupIdList)
                                    }
                                    i++
                                }
                            })
                        }
                    }

                }

            })


    }

    fun deleteGroup(groupId: String) {
        firebaseUser.child(UserObject.uid).child("GroupInfo").child(groupId).removeValue()
        firebaseFolder.child(groupId).child("MemberInfo").child(UserObject.uid).removeValue()
        FolderObject.folderInfo.remove(groupId)
        FolderObject.folderColor.remove(groupId)
    }

}