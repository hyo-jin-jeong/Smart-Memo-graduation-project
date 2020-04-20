package com.kakao.smartmemo.Contract

interface TodoDialogContract {
    interface View {

    }
    interface Presenter {
        fun setTodoDialogAdapterModel(model: TodoDialogAdapterContract.Model)
        fun setTodoDialogAdapterView(view: TodoDialogAdapterContract.View)
    }
}