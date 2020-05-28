package com.kakao.smartmemo.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakao.smartmemo.Contract.MemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Object.GroupObject

class MemoModel {
    private lateinit var onMemoListener: MemoContract.OnMemoListener
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val memoPath = firestore.collection("Memo")
    constructor()
    constructor(onMemoListener: MemoContract.OnMemoListener) {
        this.onMemoListener = onMemoListener
    }
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
        var memoList = mutableListOf<MemoData>()
        firestore.collection("Group").addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.forEach { snapShot->
                GroupObject.groupInfo.forEach {
                    if(snapShot.id == it.key ){
                        snapShot.reference.collection("MemoInfo").document("MemoId").addSnapshotListener { documentSnapshot, _ ->
                            documentSnapshot?.data?.forEach {data ->
                                memoPath.document(data.key).addSnapshotListener { memoSnapshot, _ ->
                                    memoSnapshot?.reference?.collection("Place")?.document("PlaceInfo")
                                        ?.addSnapshotListener { placeSnapshot, _ ->
                                            memoList.add(
                                                MemoData(memoSnapshot?.get("title").toString(),memoSnapshot?.get("date").toString(),memoSnapshot?.get("content").toString()
                                                    ,memoSnapshot?.get("groupName").toString(),snapShot.id,snapShot.get("group_color").toString().toLong(),
                                                    placeSnapshot?.get("placeName").toString(),
                                                    placeSnapshot?.get("latitude").toString().toDouble(),placeSnapshot?.get("longitude").toString().toDouble()))
                                                    onMemoListener.onSuccess(memoList)
                                        }
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