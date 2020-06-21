package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AddFolderContract
import com.kakao.smartmemo.Model.FolderModel

class AddGroupPresenter : AddFolderContract.Presenter{
    
    private  var folderModel : FolderModel
    private var view : AddFolderContract.View

    constructor(view: AddFolderContract.View){
        this.view = view
        this.folderModel = FolderModel()
    }

    override fun addGroup(groupId:String,group_name: String, color: Int) {
       folderModel.addGroup(groupId,group_name, color)
    }
}