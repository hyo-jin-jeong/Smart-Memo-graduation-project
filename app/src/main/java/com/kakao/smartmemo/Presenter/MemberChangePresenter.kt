package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MemberChangeContract
import com.kakao.smartmemo.Model.UserModel

class MemberChangePresenter : MemberChangeContract.Presenter{
    private var view : MemberChangeContract.View
    var userModel: UserModel

    constructor(view:MemberChangeContract.View){
        this.view= view
        this.userModel = UserModel()
    }

    override fun getProfile(email:String) {
        userModel.getProfile(email)
    }

    override fun updateUser() {
        userModel.updateUser()
    }



}