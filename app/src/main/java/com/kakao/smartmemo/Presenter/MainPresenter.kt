package com.kakao.smartmemo.Presenter

import android.util.Log
import com.kakao.smartmemo.Contract.MainAdapterContract
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Model.GroupModel

class MainPresenter : MainContract.Presenter,MainContract.onGetGroupInfoListener{

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

    override fun getGroupInfo() {
        groupModel.getGroupInfo()
    }


    override fun onSuccess(groupInfoList: MutableList<String>) {
        view.setNavigationView(groupInfoList)
    }

    override fun onFailure() {
        Log.e("groupdata 받아오기 실패", "실패!!!")
    }

}