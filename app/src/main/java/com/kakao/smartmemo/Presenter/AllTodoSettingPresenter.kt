package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AllTodoSettingContract
import com.kakao.smartmemo.Contract.TodoDateAdapterContract
import com.kakao.smartmemo.Contract.TodoPlaceAdapterContract
import com.kakao.smartmemo.Model.TodoModel

class AllTodoSettingPresenter: AllTodoSettingContract.Presenter {
    private var todoModel : TodoModel
    private var view : AllTodoSettingContract.View
    private lateinit var dateAdapterView: TodoDateAdapterContract.View
    private lateinit var dateAdapterModel: TodoDateAdapterContract.Model
    private lateinit var placeListAdapterModel: TodoPlaceAdapterContract.Model
    private lateinit var placeListAdapterView: TodoPlaceAdapterContract.View

    constructor(view: AllTodoSettingContract.View){
        this.view = view
        this.todoModel = TodoModel()
    }

    override fun addTodo() {

    }

    override fun deleteTodo() {

    }

    override fun updateTodo() {

    }

    override fun setTodoDateAdapterView(dateAdapterView: TodoDateAdapterContract.View) {
        this.dateAdapterView = dateAdapterView
    }

    override fun setTodoDateAdapterModel(dateAdapterModel: TodoDateAdapterContract.Model) {
        this.dateAdapterModel = dateAdapterModel
    }

    override fun setTodoPlaceAdapterModel(placeListAdapterModel: TodoPlaceAdapterContract.Model) {
        this.placeListAdapterModel = placeListAdapterModel
    }

    override fun setTodoPlaceAdapterView(placeListAdapterView: TodoPlaceAdapterContract.View) {
        this.placeListAdapterView = placeListAdapterView
    }


}