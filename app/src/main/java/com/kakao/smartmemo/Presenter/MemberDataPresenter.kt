package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MemberDataContract
import com.kakao.smartmemo.Model.UserModel

class MemberDataPresenter : MemberDataContract.Presenter, MemberDataContract.OnDeleteUserListener{
    private var view : MemberDataContract.View
    private var userModel: UserModel

    constructor(view:MemberDataContract.View){
        this.view= view
        this.userModel = UserModel(this)
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

    override fun onSuccess() {

    }

    override fun onFailure() {

    }

}