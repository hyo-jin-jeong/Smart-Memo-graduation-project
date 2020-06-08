package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AddGroupContract
import com.kakao.smartmemo.Model.FolderModel


class AddGroupPresenter : AddGroupContract.Presenter{
    
    private  var folderModel : FolderModel
    private var view : AddGroupContract.View


    constructor(view: AddGroupContract.View){
        this.view = view
        this.folderModel = FolderModel()
    }

    override fun addGroup(group_name: String, color: Int) {
        //GroupObject에 데이터 세팅
       folderModel.addGroup(group_name, color)
    }


}