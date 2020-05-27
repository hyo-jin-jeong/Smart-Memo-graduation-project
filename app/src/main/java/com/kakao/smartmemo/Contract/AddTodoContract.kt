package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.TodoData

interface AddTodoContract {
    interface View {

    }
    interface Presenter {
        fun addTodo(todoData: TodoData)
        fun setTodoDateAdapterModel(adapterModel : TodoDateAdapterContract.Model)
        fun setTodoDateAdapterView(adapterView : TodoDateAdapterContract.View)
        fun setTodoPlaceAdapterModel(adapterModel : TodoPlaceAdapterContract.Model)
        fun setTodoPlaceAdapterView(adapterView : TodoPlaceAdapterContract.View)
    }
}