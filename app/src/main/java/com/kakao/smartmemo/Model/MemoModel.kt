package com.kakao.smartmemo.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.smartmemo.Contract.MemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Object.FolderObject

class MemoModel {
    private lateinit var onMemoListener: MemoContract.OnMemoListener
    private var firebaseMemo = FirebaseDatabase.getInstance().reference.child("Memo")
    private var firebaseGroup = FirebaseDatabase.getInstance().reference.child("Group")
    constructor()
    constructor(onMemoListener: MemoContract.OnMemoListener) {
        this.onMemoListener = onMemoListener
    }

    fun addMemo(memoData: MemoData) {
        var memoId = ""
        memoId = if (memoData.memoId == "") {
            (System.currentTimeMillis()*3000).toInt().toString()
        } else {
            memoData.memoId
        }
        firebaseGroup.child(memoData.groupId).child("MemoInfo").updateChildren(mapOf(memoId to memoId))
        firebaseMemo.child(memoId).setValue(memoData)
    }

    fun getAllMemo() {
        var i = 0
        var j = 0
        var bool = false
        var memoList = mutableListOf<MemoData>()
        FolderObject.folderInfo.forEach {
            firebaseGroup.child(it.key).child("MemoInfo").addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(memoIdSnapshot: DataSnapshot) {
                    if(memoIdSnapshot.children.count()!=0){
                        j=0
                        for(memoId in memoIdSnapshot.children){
                            firebaseMemo.child(memoId.value.toString()).addValueEventListener(object :ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(memoSnapshot: DataSnapshot) {
                                        memoList.add(MemoData(memoId.value.toString(),memoSnapshot.child("title").value.toString(),
                                            memoSnapshot.child("date").value.toString(),memoSnapshot.child("content").value.toString()
                                            ,memoSnapshot.child("groupId").value.toString(),
                                            memoSnapshot.child("placeName").value.toString(),
                                            memoSnapshot.child("latitude").value.toString(),
                                            memoSnapshot.child("longitude").value.toString()))
                                        if(i == FolderObject.folderInfo.size-1&&j == memoIdSnapshot.children.count()-1){
                                            onMemoListener.onSuccess(memoList)
                                            i=0
                                        }else if(j == memoIdSnapshot.children.count()-1){
                                            j=0
                                            i++
                                        }else{
                                            j++
                                        }
                                    }
                            })
                        }
                    }else{
                        if(i == FolderObject.folderInfo.size-1){
                            i=0
                            onMemoListener.onSuccess(memoList)
                        }
                        i++
                    }
                }
            })
        }

    }


    fun deleteMemo(deleteMemoList: MutableList<MemoData>) {
        deleteMemoList.forEach{memoId->
            firebaseGroup.child(memoId.groupId).child("MemoInfo").child(memoId.memoId).removeValue()
            firebaseMemo.child(memoId.memoId).removeValue()
        }
    }


    fun getGroupMemo(groupId: String) {

       // onMemoListener.onSuccess(memoList)

    }

    fun deleteMemoInfo(groupId: String, memoId: String) {

    }
}