package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.DialogAdapterContract
import com.kakao.smartmemo.Contract.DialogContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Data.PlaceAlarmData
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Model.MemoModel
import com.kakao.smartmemo.Model.TodoModel

class DialogPresenter : DialogContract.Presenter , DialogContract.OnDialogListener{
    var view : DialogContract.View
    var memoModel : MemoModel
    var todoModel : TodoModel
    private lateinit var adapterView : DialogAdapterContract.View
    private lateinit var adapterModel : DialogAdapterContract.Model

    constructor(view : DialogContract.View) {
        this.view = view
        this.memoModel = MemoModel()
        this.todoModel = TodoModel()
    }
    override fun setDialogAdapterModel(adapterModel: DialogAdapterContract.Model) {
        this.adapterModel = adapterModel
    }

    override fun setDialogAdapterView(adapterView: DialogAdapterContract.View) {
        this.adapterView = adapterView
    }

    override fun getMemoList(memo: MutableList<PlaceData>) {
        memoModel.getMapDialogMemo(memo)
    }

    override fun getTodoList(todo: MutableList<PlaceData>) {
        todoModel.getMapDialogTodo(todo)
    }


    override fun onSuccessMemo(memoList: MutableList<MemoData>) {
        view.onSuccessMemo(memoList)
    }

    override fun onSuccessTodo(todoList: MutableList<PlaceAlarmData>) {
        view.onSuccessTodo(todoList)
    }
}