package com.kakao.smartmemo.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakao.smartmemo.Data.MemoData

class MemoModel {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addMemo(memoData: MemoData) {
        var memoId = memoData.title+System.currentTimeMillis()
        val memoPath = firestore.collection("Memo").document("${memoId}")

        memoPath.set(hashMapOf("title" to memoData.title, "date" to memoData.date, "content" to memoData.content,"groupName" to memoData.groupName), SetOptions.merge())
        memoPath.collection("Place").document("PlaceInfo")
            .set(hashMapOf("placeName" to "${memoData.placeName}" ,"latitude" to "${memoData.latitude}", "longitude" to  "${memoData.longitude}"),
                SetOptions.merge())

    }
    fun deleteMemo() {

    }
    fun updateMemo() {

    }
}