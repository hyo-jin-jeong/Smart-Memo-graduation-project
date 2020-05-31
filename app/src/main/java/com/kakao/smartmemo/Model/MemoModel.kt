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
            .set(hashMapOf("placeName" to memoData.placeName,"latitude" to memoData.latitude, "longitude" to  memoData.longitude),
                SetOptions.merge())
        firestore.collection("Group").document(memoData.groupId).collection("MemoInfo").document("MemoId").set(
            hashMapOf(memoId to memoId), SetOptions.merge())
    }

    fun getAllMemo(){
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
                                            if (placeSnapshot != null) {
                                                memoList.add(
                                                    MemoData(memoSnapshot?.get("title").toString(),memoSnapshot?.get("date").toString(),memoSnapshot?.get("content").toString()
                                                        ,memoSnapshot?.get("groupName").toString(),snapShot.id,snapShot.get("group_color").toString().toLong(),
                                                        placeSnapshot?.get("placeName").toString(),
                                                        placeSnapshot?.get("latitude").toString(),
                                                        placeSnapshot?.get("longitude").toString()
                                                    ))
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
    fun updateMemo() {

    }

    fun getGroupMemo(groupId: String) {
        var groupColor : Long = 0
        var memoList = mutableListOf<MemoData>()
        firestore.collection("Group").document(groupId).addSnapshotListener { groupColorSnapshot, _ ->
           groupColor = groupColorSnapshot?.get("group_color") as Long
        }
        firestore.collection("Group").document(groupId).collection("MemoInfo").document("MemoId").addSnapshotListener { memoSnapshot, _ ->
            memoSnapshot?.data?.forEach { data ->
                memoPath.document(data.key).addSnapshotListener { memoInfoSnapshot, firebaseFirestoreException ->
                    memoInfoSnapshot?.reference?.collection("Place")?.document("PlaceInfo")
                        ?.addSnapshotListener { placeSnapshot, _ ->
                            if (placeSnapshot != null) {
                                memoList.add(
                                    MemoData(memoInfoSnapshot?.get("title").toString(),memoInfoSnapshot?.get("date").toString(),memoInfoSnapshot?.get("content").toString()
                                        ,memoInfoSnapshot?.get("groupName").toString(),groupId,groupColor,
                                        placeSnapshot?.get("placeName").toString(),
                                        placeSnapshot?.get("latitude").toString(),
                                        placeSnapshot?.get("longitude").toString()
                                    ))
                                onMemoListener.onSuccess(memoList)
                            }
                        }
                }
            }
        }
    }
}