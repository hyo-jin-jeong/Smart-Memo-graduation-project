package com.kakao.smartmemo.Presenter

import android.app.Activity
import com.kakao.smartmemo.Contract.LoginContract
import com.kakao.smartmemo.Model.UserModel

class LoginPresenter : LoginContract.Presenter {
    var userModel:UserModel
    private var view : LoginContract.View


    constructor(view: LoginContract.View){
        this.view = view
        this.userModel = UserModel()
    }

    override fun checkUser(context: Activity, email:String, password:String) {
        userModel.checkUser(context, email, password)
        userModel.getProfile()
    }


}