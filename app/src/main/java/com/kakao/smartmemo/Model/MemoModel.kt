package com.kakao.smartmemo.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        firebaseGroup.child(memoData.groupId).child("MemoInfo")
            .updateChildren(mapOf(memoData.memoId to memoData.memoId))
        firebaseMemo.child(memoData.memoId).setValue(memoData)
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
                            memoIdSnapshot.children.forEach { memoId->
                                firebaseMemo.child(memoId.value.toString())
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {}
                                        override fun onDataChange(memoSnapshot: DataSnapshot) {
                                            memoSnapshot.getValue(MemoData::class.java)?.let { it1 ->
                                                memoList.add(
                                                    it1
                                                )
                                            }
                                            if (i == FolderObject.folderInfo.size - 1 && j == memoIdSnapshot.children.count()-1) {
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

    fun getPlaceMemo() {
        var i = 0
        var j = 0
        var bool = false
        var placeList = mutableListOf<PlaceData>()
        FolderObject.folderInfo.forEach {
            firebaseGroup.child(it.key).child("MemoInfo")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(memoIdSnapshot: DataSnapshot) {
                        if (memoIdSnapshot.children.count() != 0) {
                            for (memoId in memoIdSnapshot.children) {
                                firebaseMemo.child(memoId.value.toString())
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {}
                                        override fun onDataChange(memoSnapshot: DataSnapshot) {
                                            if(memoSnapshot.hasChildren()){
                                                placeList.add(
                                                    PlaceData(
                                                        memoSnapshot.child("placeId").value.toString(),
                                                        memoSnapshot.child("placeName").value.toString(),
                                                        memoSnapshot.child("latitude").value.toString().toDouble(),
                                                        memoSnapshot.child("longitude").value.toString().toDouble()
                                                    )
                                                )
                                                if (i == FolderObject.folderInfo.size - 1 && j == memoIdSnapshot.children.count() - 1) {
                                                    onPlaceListener.onSuccess(placeList, "memo")
                                                    i = 0
                                                } else if (j == memoIdSnapshot.children.count() - 1) {
                                                    j = 0
                                                    i++
                                                } else {
                                                    j++
                                                }
                                            }

                                        }
                                    })
                            }
                        } else {
                            if (i == FolderObject.folderInfo.size - 1) {
                                i = 0
                                onPlaceListener.onSuccess(placeList, "memo")
                            }
                            i++
                        }
                    }
                })
        }
    }

    fun deleteMemo(deleteMemoList: MutableList<MemoData>) {
        var i = 0
        deleteMemoList.forEach { memoId ->
            firebaseGroup.child(memoId.groupId).child("MemoInfo").child(memoId.memoId).removeValue()
            firebaseGroup.child(memoId.groupId).child("MemoInfo").child(memoId.memoId).removeValue()
            firebaseMemo.child(memoId.memoId).removeValue()
            if (i == deleteMemoList.size -1) {
                onMemoListener.onDeleteSuccess()
            } else {
                i++
            }
        }
    }


    fun getFolderMemo(folderId: String) {
        var j = 0
        var memoList = mutableListOf<MemoData>()
        firebaseGroup.child(folderId).child("MemoInfo")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(memoIdSnapshot: DataSnapshot) {
                    memoIdSnapshot.children.forEach { memoId ->
                        firebaseMemo.child(memoId.key.toString())
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}

                                override fun onDataChange(groupMemoSnapshot: DataSnapshot) {
                                    with(groupMemoSnapshot) {
                                        this.getValue(MemoData::class.java)?.let { memoList.add(it) }

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
        firebaseGroup.child(groupId).child("MemoInfo").child(memoId).removeValue()
    }

    fun deleteOneMemo(memoData: MemoData) {
        firebaseMemo.child(memoData.memoId).removeValue()
        firebaseGroup.child(memoData.groupId).child("MemoInfo").child(memoData.memoId).removeValue()
    }
}

