package com.kakao.smartmemo.Contract

import com.google.firebase.firestore.FirebaseFirestoreException
import com.kakao.smartmemo.Data.TodoData

interface TodoContract {

    interface View {
        fun showAllTodo(todoData: MutableList<TodoData>)
    }

    interface Presenter {
        fun getGroupTodo(groupId: String)
        fun setTodoAdapterModel(adapterModel : TodoAdapterContract.Model)
        fun setTodoAdapterView(adapterView : TodoAdapterContract.View)
        fun setTodoDeleteAdapterModel(deleteAdapterModel : TodoDeleteAdapterContract.Model)
        fun setTodoDeleteAdapterView(deleteAdapterView : TodoDeleteAdapterContract.View)
        fun getTodo()
    }

    interface OnTodoListener {
        fun onSuccess(todoData: MutableList<TodoData>)
        fun onFailure()
    }
}