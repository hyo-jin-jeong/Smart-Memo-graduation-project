package com.kakao.smartmemo.Presenter

import android.util.Log
import com.kakao.smartmemo.Contract.MainAdapterContract
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Model.GroupModel

class MainPresenter : MainContract.Presenter,MainContract.OnGetDataSuccessListener{

    private var view : MainContract.View
    var groupModel: GroupModel
    private lateinit var adapterView: MainAdapterContract.View
    private lateinit var adapterModel: MainAdapterContract.Model

    constructor(view: MainContract.View){
        this.view = view
        this.groupModel = GroupModel(this)
    }

    override fun setMainAdapterModel(model: MainAdapterContract.Model) {
        adapterModel = model
    }

    override fun setMainAdapterView(view: MainAdapterContract.View) {
        adapterView = view
    }

    override fun getGroupData() {
        groupModel.getGroupData()
    }

    override fun onSuccess(name: MutableList<String>) {
        view.setNavigationView(name)
    }

    override fun onFailure() {
        Log.e("groupdate 받아오기 실패", "실패!!!")
    }

}