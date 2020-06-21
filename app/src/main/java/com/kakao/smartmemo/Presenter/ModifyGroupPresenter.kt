package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.ModifyGroupContract
import com.kakao.smartmemo.Model.FolderModel

class ModifyGroupPresenter : ModifyGroupContract.Presenter{
    
    private  var folderModel : FolderModel
    private var view : ModifyGroupContract.View

    constructor(view: ModifyGroupContract.View){
        this.view = view
        this.folderModel = FolderModel()
    }

    override fun updateGroup(groupId: String, groupName: String, groupColor: Long) {
        folderModel.updateGroup(groupId,groupName,groupColor)
    }

    override fun deleteGroup(groupId: String) {
        folderModel.deleteGroup(groupId)
    }
}