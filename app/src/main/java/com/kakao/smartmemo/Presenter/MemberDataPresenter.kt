package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MemberDataContract
import com.kakao.smartmemo.Model.UserModel

class MemberDataPresenter : MemberDataContract.Presenter{
    private var view : MemberDataContract.View
    var userModel: UserModel

    constructor(view:MemberDataContract.View){
        this.view= view
        this.userModel = UserModel()
    }

    override fun getProfile() {
        userModel.getProfile()
    }

    override fun signOutUser() {
        userModel.signOutUser()
    }

    override fun checkPassword(confirmPassword: String) :Boolean{
        return userModel.checkPassword(confirmPassword)
    }

    override fun deleteUser() {
        userModel.deleteUser()
        userModel.deleteAuth()
    }

}