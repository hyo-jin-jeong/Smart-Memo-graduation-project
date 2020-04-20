package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.TodoDateAdapterContract
import com.kakao.smartmemo.Contract.TodoPlaceAdapterContract
import com.kakao.smartmemo.Contract.TodoSettingContract

class TodoSettingPresenter : TodoSettingContract.Presenter {

    var view : TodoSettingContract.View
    lateinit var dateAdapterView : TodoDateAdapterContract.View
    lateinit var dateAdapterModel : TodoDateAdapterContract.Model
    lateinit var placeAdapterView : TodoPlaceAdapterContract.View
    lateinit var placeAdapterModel : TodoPlaceAdapterContract.Model


    constructor(view: TodoSettingContract.View) {
        this.view =  view
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