package com.kakao.smartmemo.Model

import com.google.firebase.database.*
import com.kakao.smartmemo.Contract.MapContract
import com.kakao.smartmemo.Contract.MemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Object.FolderObject

class MemoModel {
    private lateinit var onMemoListener: MemoContract.OnMemoListener
    private lateinit var onPlaceListener: MapContract.OnPlaceListener
    private var firebaseMemo = FirebaseDatabase.getInstance().reference.child("Memo")
    private var firebaseGroup = FirebaseDatabase.getInstance().reference.child("Group")

    constructor()
    constructor(onMemoListener: MemoContract.OnMemoListener) {
        this.onMemoListener = onMemoListener
    }
    constructor(onPlaceListener: MapContract.OnPlaceListener) {
        this.onPlaceListener = onPlaceListener
    }

    fun addMemo(memoData: MemoData) {
        var memoId = ""
        memoId = if (memoData.memoId == "") {
            (System.currentTimeMillis() * 3000).toInt().toString()
        } else {
            memoData.memoId
        }
        firebaseGroup.child(memoData.groupId).child("MemoInfo")
            .updateChildren(mapOf(memoId to memoId))
        firebaseMemo.child(memoId).setValue(memoData)
    }

    fun getAllMemo() {
        var i = 0
        var j = 0
        var bool = false
        var memoList = mutableListOf<MemoData>()
        FolderObject.folderInfo.forEach {
            firebaseGroup.child(it.key).child("MemoInfo")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(memoIdSnapshot: DataSnapshot) {
                        if (memoIdSnapshot.children.count() != 0) {
                            j = 0
                            for (memoId in memoIdSnapshot.children) {
                                firebaseMemo.child(memoId.value.toString())
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {}
                                        override fun onDataChange(memoSnapshot: DataSnapshot) {
                                            memoList.add(
                                                MemoData(
                                                    memoId.value.toString(),
                                                    memoSnapshot.child("title").value.toString(),
                                                    memoSnapshot.child("date").value.toString(),
                                                    memoSnapshot.child("content").value.toString()
                                                    ,
                                                    memoSnapshot.child("groupId").value.toString(),
                                                    memoSnapshot.child("placeName").value.toString(),
                                                    memoSnapshot.child("latitude").value.toString(),
                                                    memoSnapshot.child("longitude").value.toString()
                                                )
                                            )
                                            if (i == FolderObject.folderInfo.size - 1 && j == memoIdSnapshot.children.count() - 1) {
                                                onMemoListener.onSuccess(memoList)
                                                i = 0
                                            } else if (j == memoIdSnapshot.children.count() - 1) {
                                                j = 0
                                                i++
                                            } else {
                                                j++
                                            }
                                        }
                                    })
                            }
                        } else {
                            if (i == FolderObject.folderInfo.size - 1) {
                                i = 0
                                onMemoListener.onSuccess(memoList)
                            }
                            i++
                        }
                    }
                })
        }

    }


    fun deleteMemo(deleteMemoList: MutableList<MemoData>) {
        deleteMemoList.forEach { memoId ->
            firebaseGroup.child(memoId.groupId).child("MemoInfo").child(memoId.memoId).removeValue()
            firebaseGroup.child(memoId.groupId).child("MemoInfo").child(memoId.memoId).removeValue()
            firebaseMemo.child(memoId.memoId).removeValue()
        }
    }


    fun getFolderMemo(folderId: String) {
        var j = 0
        var memoList = mutableListOf<MemoData>()
        firebaseGroup.child(folderId).child("MemoInfo").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(memoIdSnapshot: DataSnapshot) {
                memoIdSnapshot.children.forEach { memoId ->
                    firebaseMemo.child(memoId.key.toString()).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {  }

                        override fun onDataChange(groupMemoSnapshot: DataSnapshot) {
                            with(groupMemoSnapshot) {
                                memoList.add(MemoData(memoId.key.toString()
                                    , this.child("title").value.toString()
                                , this.child("date").value.toString()
                                , this.child("content").value.toString()
                                , this.child("groupId").value.toString()
                                , this.child("placeName").value.toString()
                                , this.child("latitude").value.toString()
                                , this.child("longitude").value.toString()
                                ))
                                if (memoList.size == memoIdSnapshot.children.count()) {
                                    onMemoListener.onSuccess(memoList)
                                }
                            }

                        }
                    })

                }
            }
        })
    }


    fun deleteMemoInfo(groupId: String, memoId: String) {


    }

    fun getPlaceMemo() {
        var i = 0
        var j = 0
        var placeList = mutableListOf<PlaceData>()
        FolderObject.folderInfo.forEach {
            firebaseGroup.child(it.key).child("MemoInfo").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) { }

                override fun onDataChange(memoSnapshot: DataSnapshot) {
                    memoSnapshot.children.forEach {memoId ->
                        firebaseMemo.child(memoId.value.toString()).addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) { }

                            override fun onDataChange(placeSnapshot: DataSnapshot) {
                                placeList.add(PlaceData(placeSnapshot.child("placeName").value.toString(),
                                    placeSnapshot.child("latitude").value.toString().toDouble(),
                                    placeSnapshot.child("longitude").value.toString().toDouble()))
                                if (i == memoSnapshot.children.count()-1 && j ==FolderObject.folderInfo.size-1) {
                                    onPlaceListener.onSuccess(placeList, "memo")
                                } else if (i == memoSnapshot.children.count()-1) {
                                    i = 0
                                    j++
                                } else {
                                    i++
                                }
                            }

                        })
                    }
                }
            })
        }
    }
}

