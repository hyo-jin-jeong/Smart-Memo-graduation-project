package com.kakao.smartmemo.Presenter

import android.app.Activity
import com.kakao.smartmemo.Contract.SignUpContract
import com.kakao.smartmemo.Model.UserModel

class SignUpPresenter : SignUpContract.Presenter, SignUpContract.onSignUpListener {
    var userModel:UserModel
    private var view : SignUpContract.View

    constructor(view: SignUpContract.View){
        this.view = view
        this.userModel = UserModel(this)
    }

    override fun addUser(context: Activity, email:String, pw:String, name:String, address:String) {
        userModel.addAuthUser(context, email, pw, name, address)
    }

    override fun addFirestoreUser() {
        userModel.addFirestoreUser()
    }
    override fun onSuccess(message: String) {
        view.onSignUpSuccess(message)
    }

    override fun onFailure(message: String) {
        view.onSignUpFailure(message)
    }
}