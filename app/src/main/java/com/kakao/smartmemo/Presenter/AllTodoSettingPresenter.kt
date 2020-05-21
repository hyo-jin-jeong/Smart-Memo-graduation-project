package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AllTodoSettingContract
import com.kakao.smartmemo.Model.TodoModel

class AllTodoSettingPresenter: AllTodoSettingContract.Presenter {
    private var todoModel : TodoModel
    private var view : AllTodoSettingContract.View


    constructor(view: AllTodoSettingContract.View){
        this.view = view
        this.todoModel = TodoModel()
    }
}