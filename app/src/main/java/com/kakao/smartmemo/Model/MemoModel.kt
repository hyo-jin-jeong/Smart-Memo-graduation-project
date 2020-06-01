package com.kakao.smartmemo.Model

import android.util.Log
import com.google.firebase.firestore.FieldValue
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
        var memoId = ""
        memoId = if (memoData.memoId == "") {
            memoData.title + System.currentTimeMillis()
        } else {
            memoData.memoId
        }
        memoPath.document(memoId).set(
            hashMapOf(
                "title" to memoData.title,
                "date" to memoData.date,
                "content" to memoData.content,
                "groupName" to memoData.groupName,
                "groupId" to memoData.groupId
            ), SetOptions.merge()
        )
        memoPath.document(memoId).collection("Place").document("PlaceInfo")
            .set(
                hashMapOf(
                    "placeName" to memoData.placeName,
                    "latitude" to memoData.latitude,
                    "longitude" to memoData.longitude
                ),
                SetOptions.merge()
            )
        firestore.collection("Group").document(memoData.groupId).collection("MemoInfo")
            .document("MemoId").set(
            hashMapOf(memoId to memoId), SetOptions.merge()
        )
    }

    fun getAllMemo() {
        var memoList = mutableListOf<MemoData>()
        firestore.collection("Group").addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.forEach { snapShot ->
                GroupObject.groupInfo.forEach {
                    if (snapShot.id == it.key) {
                        snapShot.reference.collection("MemoInfo").document("MemoId")
                            .addSnapshotListener { documentSnapshot, _ ->
                                documentSnapshot?.data?.forEach { data ->
                                    memoPath.document(data.key)
                                        .addSnapshotListener { memoSnapshot, _ ->
                                            memoSnapshot?.reference?.collection("Place")
                                                ?.document("PlaceInfo")
                                                ?.addSnapshotListener { placeSnapshot, _ ->
                                                    if (placeSnapshot != null) {
                                                        memoList.add(
                                                            MemoData(
                                                                memoSnapshot.id,
                                                                memoSnapshot?.get("title")
                                                                    .toString(),
                                                                memoSnapshot?.get("date")
                                                                    .toString(),
                                                                memoSnapshot?.get("content")
                                                                    .toString()
                                                                ,
                                                                memoSnapshot?.get("groupName")
                                                                    .toString(),
                                                                memoSnapshot?.get("groupId")
                                                                    .toString(),
                                                                placeSnapshot?.get("placeName")
                                                                    .toString(),
                                                                placeSnapshot?.get("latitude")
                                                                    .toString(),
                                                                placeSnapshot?.get("longitude")
                                                                    .toString()
                                                            )
                                                        )
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
    }

    fun deleteMemo() {

    }


    fun getGroupMemo(groupId: String) {
        var memoList = mutableListOf<MemoData>()
        firestore.collection("Group").document(groupId).collection("MemoInfo").document("MemoId")
            .addSnapshotListener { memoSnapshot, _ ->
                if (memoSnapshot != null) {
                    Log.e("memoSnapshot",memoSnapshot.data.toString())
                    if (memoSnapshot.data!=null) {
                        memoSnapshot?.data?.forEach { data ->
                            memoPath.document(data.key).addSnapshotListener { memoInfoSnapshot, _ ->
                                memoInfoSnapshot?.reference?.collection("Place")
                                    ?.document("PlaceInfo")
                                    ?.addSnapshotListener { placeSnapshot, _ ->
                                        if (placeSnapshot != null) {
                                            memoList.add(
                                                MemoData(
                                                    memoSnapshot?.get("memoId").toString(),
                                                    memoInfoSnapshot?.get("title").toString(),
                                                    memoInfoSnapshot?.get("date").toString(),
                                                    memoInfoSnapshot?.get("content").toString()
                                                    ,
                                                    memoInfoSnapshot?.get("groupName").toString(),
                                                    placeSnapshot?.get("placeName").toString(),
                                                    placeSnapshot?.get("latitude").toString(),
                                                    placeSnapshot?.get("longitude").toString()
                                                )
                                            )
                                            onMemoListener.onSuccess(memoList)
                                        }
                                    }
                            }
                        }
                    }else{
                        onMemoListener.onSuccess(memoList)
                    }
                }
            }

    }

    fun deleteMemoInfo(groupId: String, memoId: String) {
        val docRef = firestore.collection("Group").document(groupId).collection("MemoInfo")
            .document("MemoId")
        val updates = hashMapOf<String, Any>(
            memoId to FieldValue.delete()
        )
        docRef.update(updates)
    }
}