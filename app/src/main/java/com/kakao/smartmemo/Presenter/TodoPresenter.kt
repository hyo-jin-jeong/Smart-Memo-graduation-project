package com.kakao.smartmemo.Presenter

import android.util.Log
import com.kakao.smartmemo.Contract.TodoAdapterContract
import com.kakao.smartmemo.Contract.TodoContract
import com.kakao.smartmemo.Contract.TodoDeleteAdapterContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Model.TodoModel

class TodoPresenter : TodoContract.Presenter, TodoContract.OnTodoListener {
    private var view : TodoContract.View
    private var todoModel : TodoModel
    private lateinit var adapterView : TodoAdapterContract.View
    private lateinit var adapterModel: TodoAdapterContract.Model
    private lateinit var deleteAdapterView : TodoDeleteAdapterContract.View
    private lateinit var deleteAdapterModel : TodoDeleteAdapterContract.Model

    constructor(view: TodoContract.View) {
        this.view = view
        this.todoModel = TodoModel(this)
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

    override fun getAllTodo() {
        todoModel.getAllTodo()
    }

    override fun getGroupTodo(groupId: String) {
        todoModel.getGroupTodo(groupId)
    }

    override fun deleteTodo(todoData: MutableList<TodoData>) {
        todoModel.deleteTodo(todoData)
    }

    override fun onSuccess(todoData: MutableList<TodoData>) {
        view.showAllTodo(todoData)
    }

    override fun onFailure() {

    }

    override fun onGroupSuccess(todoList: MutableList<TodoData>, placeList: MutableList<PlaceData>) {
        view.showAllTodo(todoList)
        view.sendPlaceData(placeList)
    }

}