package com.kakao.smartmemo.Presenter

import android.util.Log
import com.kakao.smartmemo.Contract.MainAdapterContract
import com.kakao.smartmemo.Contract.MainContract
import com.kakao.smartmemo.Model.FolderModel
import com.kakao.smartmemo.Model.UserModel

class MainPresenter : MainContract.Presenter,MainContract.onMainListener{

    private var view : MainContract.View
    var folderModel: FolderModel
    var userModel:UserModel
    private lateinit var adapterView: MainAdapterContract.View
    private lateinit var adapterModel: MainAdapterContract.Model

    constructor(view: MainContract.View){
        this.view = view
        this.folderModel = FolderModel(this)
        this.userModel = UserModel(this)
    }

    override fun setMainAdapterModel(model: MainAdapterContract.Model) {
        adapterModel = model
    }

    override fun setMainAdapterView(view: MainAdapterContract.View) {
        adapterView = view
    }

    override fun getGroupInfo() {
        folderModel.getGroupInfo()
    }

    override fun checkFolderMember(groupId: String?, groupName: String?) {
        userModel.checkFolderMember(groupId,groupName)
    }



    override fun onSuccess() {
        view.onSuccess()
    }

    override fun onFailure() {
        Log.e("groupdata 받아오기 실패", "실패!!!")
    }

}