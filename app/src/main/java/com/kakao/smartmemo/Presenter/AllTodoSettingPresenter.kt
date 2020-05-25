package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.*
import com.kakao.smartmemo.Model.TodoModel

class AllTodoSettingPresenter: AllTodoSettingContract.Presenter {
    private var todoModel : TodoModel
    private var view : AllTodoSettingContract.View
    lateinit var dateAdapterView : TodoDateAdapterContract.View
    lateinit var dateAdapterModel : TodoDateAdapterContract.Model
    lateinit var placeAdapterView : TodoPlaceAdapterContract.View
    lateinit var placeAdapterModel : TodoPlaceAdapterContract.Model

    constructor(view: AllTodoSettingContract.View){
        this.view = view
        this.todoModel = TodoModel()
    }

    override fun setTodoDateAdapterModel(adapterModel: TodoDateAdapterContract.Model) {
        dateAdapterModel = adapterModel
    }

    override fun setTodoDateAdapterView(adapterView: TodoDateAdapterContract.View) {
        dateAdapterView = adapterView
    }

    override fun setTodoPlaceAdapterModel(adapterModel: TodoPlaceAdapterContract.Model) {
        placeAdapterModel = adapterModel
    }

    override fun setTodoPlaceAdapterView(adapterView: TodoPlaceAdapterContract.View) {
        placeAdapterView = adapterView
    }
}