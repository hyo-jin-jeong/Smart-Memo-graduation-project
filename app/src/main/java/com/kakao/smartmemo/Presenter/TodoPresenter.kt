package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.TodoAdapterContract
import com.kakao.smartmemo.Contract.TodoContract
import com.kakao.smartmemo.Contract.TodoDeleteAdapterContract
import com.kakao.smartmemo.Model.TodoModel

class TodoPresenter : TodoContract.Presenter {
    private var view : TodoContract.View
    private var todoModel : TodoModel
    private lateinit var adapterView : TodoAdapterContract.View
    private lateinit var adapterModel: TodoAdapterContract.Model
    private lateinit var deleteAdapterView : TodoDeleteAdapterContract.View
    private lateinit var deleteAdapterModel : TodoDeleteAdapterContract.Model

    constructor(view: TodoContract.View) {
        this.view = view
        this.todoModel = TodoModel()
    }

    override fun getGroup() {

    }

    override fun getTodo() {
        todoModel.getTodo()
    }

    override fun setTodoAdapterModel(adapterModel: TodoAdapterContract.Model) {
        this.adapterModel = adapterModel
    }

    override fun setTodoAdapterView(adapterView: TodoAdapterContract.View) {
        this.adapterView = adapterView
    }

    override fun setTodoDeleteAdapterModel(deleteAdapterModel: TodoDeleteAdapterContract.Model) {
        this.deleteAdapterModel = deleteAdapterModel
    }

    override fun setTodoDeleteAdapterView(deleteAdapterView: TodoDeleteAdapterContract.View) {
        this.deleteAdapterView = deleteAdapterView
    }

}