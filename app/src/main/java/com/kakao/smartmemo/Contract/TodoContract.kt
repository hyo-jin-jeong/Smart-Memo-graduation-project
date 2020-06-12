package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Data.TodoData

interface TodoContract {

    interface View {
        fun showAllTodo(todoData: MutableList<TodoData>)
        fun sendPlaceData(placeList: MutableList<PlaceData>)
    }

    interface Presenter {
        fun getGroupTodo(groupId: String)
        fun setTodoAdapterModel(adapterModel : TodoAdapterContract.Model)
        fun setTodoAdapterView(adapterView : TodoAdapterContract.View)
        fun setTodoDeleteAdapterModel(deleteAdapterModel : TodoDeleteAdapterContract.Model)
        fun setTodoDeleteAdapterView(deleteAdapterView : TodoDeleteAdapterContract.View)
        fun getAllTodo()
        fun deleteTodo(todoData: MutableList<TodoData>)
        fun getOnePlaceTodo(todoId: String)
    }

    interface OnTodoListener {
        fun onSuccess(todoData: MutableList<TodoData>)
        fun onFailure()
        fun onGroupSuccess(todoList: MutableList<TodoData>)
        fun onOnePlaceSuccess(placeList: MutableList<PlaceData>)
    }
}