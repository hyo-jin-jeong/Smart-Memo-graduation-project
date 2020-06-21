package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.TodoDialogAdapterContract
import com.kakao.smartmemo.Contract.TodoDialogContract

class TodoDialogPresenter: TodoDialogContract.Presenter {
    var view : TodoDialogContract.View
    private lateinit var adapterModel: TodoDialogAdapterContract.Model
    private lateinit var adapterView: TodoDialogAdapterContract.View

    constructor(view: TodoDialogContract.View) {
        this.view = view
    }

    override fun setTodoDialogAdapterModel(model: TodoDialogAdapterContract.Model) {
        adapterModel = model
    }

    override fun setTodoDialogAdapterView(view: TodoDialogAdapterContract.View) {
        adapterView = view
    }
}