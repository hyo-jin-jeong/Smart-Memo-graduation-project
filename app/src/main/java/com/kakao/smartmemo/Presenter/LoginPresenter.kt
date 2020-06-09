package com.kakao.smartmemo.Presenter

import android.app.Activity
import android.util.Log
import com.kakao.smartmemo.Contract.LoginContract
import com.kakao.smartmemo.Model.UserModel

class LoginPresenter : LoginContract.Presenter, LoginContract.OnLoginListener {
    var userModel:UserModel
    private var view : LoginContract.View

    constructor(view: LoginContract.View){
        this.view = view
        this.userModel = UserModel(this)
    }

    override fun checkUser(context: Activity, email:String, password:String) {
        userModel.checkUser(context, email, password)
    }
    override fun getProfile() {
        Log.e("그만해","ㅇㅇ")
        userModel.getProfile()
    }

    override fun onSuccess(message: String) {
        view.onLoginSuccess(message)
    }

    override fun onFailure(message: String) {
        view.onLoginFailure(message)
    }

}