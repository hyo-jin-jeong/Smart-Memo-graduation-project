package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.SignInContract
import com.kakao.smartmemo.Model.UserModel

class SignInPresenter : SignInContract.Presenter {
    var userModel:UserModel
    private var view : SignInContract.View


    constructor(view: SignInContract.View){
        this.view = view
        this.userModel = UserModel()
    }

    override fun addUser() {
        userModel.addUser()

    }


}