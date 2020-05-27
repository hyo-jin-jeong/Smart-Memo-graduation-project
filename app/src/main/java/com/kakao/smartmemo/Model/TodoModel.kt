package com.kakao.smartmemo.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakao.smartmemo.Data.TodoData

class TodoModel {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addTodo(todoData: TodoData) {
        var todoId = todoData.title+System.currentTimeMillis()
        val todoPath = firestore.collection("Todo").document("${todoId}")
        todoPath.set(hashMapOf("title" to todoData.title, "groupName" to todoData.groupName), SetOptions.merge())
        todoPath.collection("TimeAlarm").document("${todoData.timeAlarmId}")
            .set(hashMapOf("setTimeAlarm" to todoData.setTimeAlarm, "timeDay" to todoData.timeDay, "timeDate" to todoData.timeDate, "timeTime" to todoData.timeTime
            , "timeAgain" to todoData.timeAgain), SetOptions.merge())
        todoPath.collection("PlaceAlarm").document("${todoData.placeAlarmId}")
            .set(hashMapOf("setPlaceAlarm" to todoData.setPlaceAlarm, "placeDate" to todoData.placeDate, "placeAgain" to todoData.placeAgain), SetOptions.merge())
        todoPath.collection("PlaceAlarm").document("${todoData.placeAlarmId}")
            .collection("Place").document("PlaceInfo")
            .set(hashMapOf("placeName" to "${todoData.placeName}", "latitude" to "${todoData.latitude}", "longitude" to "${todoData.longitude}"), SetOptions.merge())
    }
    fun deleteTodo() {

    }
    fun updateTodo() {

    }
}