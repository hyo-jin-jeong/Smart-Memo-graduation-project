package com.kakao.smartmemo.Model

import android.util.Log
import com.google.firebase.firestore.*
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Object.GroupObject

class TodoModel {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var todoPath = firestore.collection("Todo")
    private var groupPath = firestore.collection("Group")

    fun getTodo() {
        var todoData = mutableListOf<TodoData>()
        groupPath.addSnapshotListener() { querySnapshot, _ ->
            querySnapshot?.forEach { query ->
                GroupObject.groupInfo.forEach {
                    if (query.id == it.key) {
                        query.reference.collection("TodoInfo").document("todoId").addSnapshotListener { documentSnapshot, _ ->
                            documentSnapshot?.data?.forEach {
                                //todoData.add(it.key)
                                todoPath.document("${it.key}").addSnapshotListener { documentSnapshot: DocumentSnapshot?, _: FirebaseFirestoreException? ->
                                    if (documentSnapshot != null) {
                                        todoData.add(TodoData(documentSnapshot.get("title").toString(), documentSnapshot.get("groupName").toString(), it.key))
//                                        Log.e("todoData", todoData.toString())
                                    }

                                }
                            }

                        }
                    }
                }
            }
        }
    }
    fun addTodo(todoData: TodoData) {
        var todoId = todoData.title+System.currentTimeMillis()
        todoPath.document("${todoId}").set(hashMapOf("title" to todoData.title, "groupName" to todoData.groupName), SetOptions.merge())
        todoPath.document("${todoId}").collection("TimeAlarm").document("${todoData.timeAlarmId}")
            .set(hashMapOf("setTimeAlarm" to todoData.setTimeAlarm, "timeDay" to todoData.timeDay, "timeDate" to todoData.timeDate, "timeTime" to todoData.timeTime
            , "timeAgain" to todoData.timeAgain), SetOptions.merge())
        todoPath.document("${todoId}").collection("PlaceAlarm").document("${todoData.placeAlarmId}")
            .set(hashMapOf("setPlaceAlarm" to todoData.setPlaceAlarm, "placeDate" to todoData.placeDate, "placeAgain" to todoData.placeAgain), SetOptions.merge())
        todoPath.document("${todoId}").collection("PlaceAlarm").document("${todoData.placeAlarmId}")
            .collection("Place").document("PlaceInfo")
            .set(hashMapOf("placeName" to "${todoData.placeName}", "latitude" to "${todoData.latitude}", "longitude" to "${todoData.longitude}"), SetOptions.merge())

        firestore.collection("Group").document("${todoData.groupId}").collection("TodoInfo").document("todoId")
            .set(hashMapOf("${todoId}" to "${todoId}"))
    }
    fun deleteTodo() {

    }
    fun updateTodo() {

    }
}