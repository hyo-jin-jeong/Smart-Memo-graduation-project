package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.AddGroupContract
import com.kakao.smartmemo.Model.GroupModel


class AddGroupPresenter : AddGroupContract.Presenter{
    
    private  var groupModel : GroupModel
    private var view : AddGroupContract.View


    constructor(view: AddGroupContract.View){
        this.view = view
        this.groupModel = GroupModel()
    }

    override fun addGroup(group_name: String, color: Int) {
        //GroupObject에 데이터 세팅
       groupModel.addGroup(group_name, color)
    }


}