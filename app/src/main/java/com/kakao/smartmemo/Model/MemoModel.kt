package com.kakao.smartmemo.Model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.smartmemo.Contract.MemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Object.GroupObject

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
        Log.e("ADD","DASDF")
    }

    fun getAllMemo() {
        var i = 0
        var j = 0
        var bool = false
        var memoList = mutableListOf<MemoData>()
        GroupObject.groupInfo.forEach {
            Log.e("memoId",it.key)
            firebaseGroup.child(it.key).child("MemoInfo").addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(memoIdSnapshot: DataSnapshot) {
                    if(memoIdSnapshot.children.count()!=0){
                        j=0
                        for(memoId in memoIdSnapshot.children){
                            firebaseMemo.child(memoId.value.toString()).addValueEventListener(object :ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(memoSnapshot: DataSnapshot) {
                                    Log.e("check",memoId.value.toString())
                                        memoList.add(MemoData(memoId.value.toString(),memoSnapshot.child("title").value.toString(),
                                            memoSnapshot.child("date").value.toString(),memoSnapshot.child("content").value.toString()
                                            ,memoSnapshot.child("groupId").value.toString(),
                                            memoSnapshot.child("placeName").value.toString(),
                                            memoSnapshot.child("latitude").value.toString(),
                                            memoSnapshot.child("longitude").value.toString()))
                                        if(i == GroupObject.groupInfo.size-1&&j == memoIdSnapshot.children.count()-1){
                                            Log.e("check1",i.toString())
                                            onMemoListener.onSuccess(memoList)
                                            i=0
                                        }else if(j == memoIdSnapshot.children.count()-1){
                                            j=0
                                            Log.e("check2",memoIdSnapshot.children.count().toString())
                                            i++
                                        }else{
                                            j++
                                        }
                                    }
                            })
                        }
                    }else{
                        if(i == GroupObject.groupInfo.size-1){
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
        deleteMemoList.forEach{memoData->
            firebaseGroup.child(memoData.groupId).child("MemoInfo").child(memoData.memoId).removeValue()
            firebaseMemo.child(memoData.memoId).removeValue()
        }
    }


    fun getGroupMemo(groupId: String) {

       // onMemoListener.onSuccess(memoList)

    }

    fun deleteMemoInfo(groupId: String, memoId: String) {

    }//update 할때 그룹을 바꾸면
}