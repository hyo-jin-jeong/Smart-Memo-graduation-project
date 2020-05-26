package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MemoAdapterContract
import com.kakao.smartmemo.Contract.MemoContract
import com.kakao.smartmemo.Contract.MemoDeleteAdapterContract

class MemoPresenter : MemoContract.Presenter{
    var view: MemoContract.View
    private lateinit var adapterView : MemoAdapterContract.View
    private lateinit var adapterModel : MemoAdapterContract.Model
    private lateinit var deleteAdapterView : MemoDeleteAdapterContract.View
    private lateinit var deleteAdapterModel : MemoDeleteAdapterContract.Model

    constructor(view: MemoContract.View) {
        this.view = view
    }

    override fun getGroup() {
    }

    override fun setMemoAdapterModel(model: MemoAdapterContract.Model) {
        adapterModel = model
    }

    override fun setMemoAdapterView(view: MemoAdapterContract.View) {
        adapterView = view
        adapterView.onClickFunc = {onClickListener(it)}
    }

    override fun setMemoDeleteAdapterModel(deleteAdapterModel: MemoDeleteAdapterContract.Model) {
        this.deleteAdapterModel = deleteAdapterModel
    }

    override fun setMemoDeleteAdapterView(deleteAdapterView: MemoDeleteAdapterContract.View) {
        this.deleteAdapterView = deleteAdapterView
    }
    private fun onClickListener(position: Int){
        adapterModel.getMemo(position).let {
            view.showMemoItem(it)
        }
    }

}