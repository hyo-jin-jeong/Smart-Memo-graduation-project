package com.kakao.smartmemo.Model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Object.GroupObject

class MemoModel {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val memoPath = firestore.collection("Memo")
    fun addMemo(memoData: MemoData) {
        var memoId = memoData.title+System.currentTimeMillis()

        memoPath.document(memoId).set(hashMapOf("title" to memoData.title, "date" to memoData.date, "content" to memoData.content,"groupName" to memoData.groupName), SetOptions.merge())
        memoPath.document(memoId).collection("Place").document("PlaceInfo")
            .set(hashMapOf("placeName" to memoData.placeName,"latitude" to "${memoData.latitude}", "longitude" to  "${memoData.longitude}"),
                SetOptions.merge())
        firestore.collection("Group").document(memoData.groupId).collection("MemoInfo").document("MemoId").set(
            hashMapOf(memoId to memoId), SetOptions.merge())
    }

    fun getMemo(){
        var i = 0
        var memoList = mutableListOf<MemoData>()
        firestore.collection("Group").addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.forEach { snapShot->
                GroupObject.groupInfo.forEach {
                    if(snapShot.id == it.key ){
                        snapShot.reference.collection("MemoInfo").document("MemoId").addSnapshotListener { documentSnapshot, _ ->
                            documentSnapshot?.data?.forEach {data ->
                                //memoList.add(data.key)
                                memoPath.document(data.key).addSnapshotListener { memoSnapshot, _ ->
                                    memoList.add(MemoData(memoSnapshot?.get("title").toString(),memoSnapshot?.get("date").toString(),memoSnapshot?.get("content").toString()
                                            ,memoSnapshot?.get("groupName").toString(),snapShot.id))
                                    i++
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fun deleteMemo() {

    }
    fun updateMemo() {

    }
}