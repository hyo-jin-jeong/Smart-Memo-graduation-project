package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MemoAdapterContract
import com.kakao.smartmemo.Contract.MemoContract
import com.kakao.smartmemo.Contract.MemoDeleteAdapterContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Model.MemoModel

class MemoPresenter : MemoContract.Presenter,MemoContract.OnMemoListener{
    var view: MemoContract.View
    var model: MemoModel
    private lateinit var adapterView : MemoAdapterContract.View
    private lateinit var adapterModel : MemoAdapterContract.Model
    private lateinit var deleteAdapterView : MemoDeleteAdapterContract.View
    private lateinit var deleteAdapterModel : MemoDeleteAdapterContract.Model


    constructor(view: MemoContract.View) {
        this.view = view
        this.model = MemoModel(this)
    }
    override fun getMemo(){
        model.getMemo()
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
            view.showMemoItem(position)
        }
    }

    override fun onSuccess(memoList:MutableList<MemoData>) {
        view.showAllMemo(memoList)
    }

    override fun onFailer() {

    }

}