package com.kakao.smartmemo.Contract

interface AllTodoSettingContract {
    interface Presenter {
        fun addTodo()
        fun deleteTodo()
        fun updateTodo()
        fun setTodoDateAdapterView(dateAdapterView: TodoDateAdapterContract.View)
        fun setTodoDateAdapterModel(dateAdapterModel: TodoDateAdapterContract.Model)
        fun setTodoPlaceAdapterModel(placeListAdapterModel: TodoPlaceAdapterContract.Model)
        fun setTodoPlaceAdapterView(placeListAdapterView: TodoPlaceAdapterContract.View)
    }
    interface View {

    }
}