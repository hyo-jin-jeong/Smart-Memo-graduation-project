package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MemoDialogAdapterContract
import com.kakao.smartmemo.Contract.MemoDialogContract

class MemoDialogPresenter : MemoDialogContract.Presenter {
    var view : MemoDialogContract.View
    private lateinit var adapterView : MemoDialogAdapterContract.View
    private lateinit var adapterModel: MemoDialogAdapterContract.Model

    constructor(view : MemoDialogContract.View) {
        this.view = view
    }

    override fun setMemoDialogAdatperModel(model: MemoDialogAdapterContract.Model) {
        adapterModel = model
    }

    override fun setMemoDialogAdatperView(view: MemoDialogAdapterContract.View) {
        adapterView = view
        adapterView.onClickFunc = {onClickListener(it)}
    }

    private fun onClickListener(position: Int){
        adapterModel.getMemo(position).let {
            view.showMemoItem(position)
        }
    }
}