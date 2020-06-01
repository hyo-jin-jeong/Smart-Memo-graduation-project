
package com.kakao.smartmemo.Model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakao.smartmemo.Contract.TodoContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Object.GroupObject

class TodoModel {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var todoPath = firestore.collection("Todo")
    private var groupPath = firestore.collection("Group")
    private lateinit var onTodoListener: TodoContract.OnTodoListener

    constructor() {}
    constructor(onTodoListener: TodoContract.OnTodoListener) {
        this.onTodoListener = onTodoListener
    }

    fun getTodo() {
        var todoData = mutableListOf<TodoData>()
        groupPath.addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.forEach { snapShot ->
                GroupObject.groupInfo.forEach {
                    if (snapShot.id == it.key) {
                        snapShot.reference.collection("TodoInfo").document("todoId").addSnapshotListener { documentSnapshot, _ ->
                            documentSnapshot?.data?.forEach { data ->
                                todoPath.document(data.key).addSnapshotListener { todoSnapshot, _ ->
                                    todoSnapshot?.reference?.collection("TimeAlarm")?.addSnapshotListener{ timeAlarmIdSnapshot, _ ->
                                        var title = todoSnapshot?.get("title").toString()
                                        timeAlarmIdSnapshot?.forEach { timeSnapshot ->
                                            todoSnapshot.reference.collection("PlaceAlarm")?.addSnapshotListener { placeAlarmIdSnapshot, _ ->
                                                placeAlarmIdSnapshot?.forEach { placeSnapshot ->
                                                    placeSnapshot.reference.collection("Place").document("PlaceInfo").addSnapshotListener { placeInfoSnapshot, _ ->
                                                        if(!placeAlarmIdSnapshot.isEmpty){
                                                            todoData.add(TodoData(title, todoSnapshot.get("groupName").toString(), it.key,
                                                                snapShot.get("group_color").toString().toLong(), timeSnapshot.id,
                                                                timeSnapshot?.get("setTimeAlarm").toString().toBoolean(),
                                                                timeSnapshot?.get("timeDate").toString(),
                                                                timeSnapshot?.get("timeTime").toString(),
                                                                timeSnapshot?.get("timeAgain").toString().toInt(),
                                                                placeSnapshot.id,
                                                                placeSnapshot.get("setPlaceAlarm").toString().toBoolean(),
                                                                placeSnapshot.get("placeDate").toString(),
                                                                placeSnapshot.get("placeAgain").toString().toInt(),
                                                                placeInfoSnapshot?.get("placeName").toString(),
                                                                placeInfoSnapshot?.get("latitude").toString(),
                                                                placeInfoSnapshot?.get("longitude").toString()
                                                            ))
                                                            onTodoListener.onSuccess(todoData)
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
                }
            }
        }
    }

    fun addTodo(todoData: TodoData) {
        var todoId = todoData.title+System.currentTimeMillis()
        Log.e("timeAgain placeAgain", "${todoData.timeAgain} ${todoData.placeAgain}")
        todoPath.document("${todoId}").set(hashMapOf("title" to todoData.title, "groupName" to todoData.groupName), SetOptions.merge())
        todoPath.document("${todoId}").collection("TimeAlarm").document("${todoData.timeAlarmId}")
            .set(hashMapOf("setTimeAlarm" to todoData.setTimeAlarm, "timeDate" to todoData.timeDate, "timeTime" to todoData.timeTime
                , "timeAgain" to todoData.timeAgain), SetOptions.merge())
        todoPath.document("${todoId}").collection("PlaceAlarm").document("${todoData.placeAlarmId}")
            .set(hashMapOf("setPlaceAlarm" to todoData.setPlaceAlarm, "placeDate" to todoData.placeDate, "placeAgain" to todoData.placeAgain), SetOptions.merge())
        todoPath.document("${todoId}").collection("PlaceAlarm").document("${todoData.placeAlarmId}")
            .collection("Place").document("PlaceInfo")
            .set(hashMapOf("placeName" to "${todoData.placeName}", "latitude" to "${todoData.latitude}", "longitude" to "${todoData.longitude}"), SetOptions.merge())

        firestore.collection("Group").document("${todoData.groupId}").collection("TodoInfo").document("todoId")
            .set(hashMapOf("${todoId}" to "${todoId}"), SetOptions.merge())
    }
    fun deleteTodo() {

    }
    fun updateTodo() {

    }

}