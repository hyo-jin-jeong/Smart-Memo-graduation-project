package com.kakao.smartmemo.Presenter

import android.app.Activity
import android.content.Context
import android.util.Log
import com.kakao.smartmemo.Contract.SignInContract
import com.kakao.smartmemo.Model.UserModel

class SignInPresenter : SignInContract.Presenter {
    var userModel:UserModel
    private var view : SignInContract.View


    constructor(view: SignInContract.View){
        this.view = view
        this.userModel = UserModel()
    }

    override fun addUser(context: Activity, email:String, pw:String, name:String, address:String) {
        var authResult = userModel.addAuthUser(context, email, pw, name, address)
        if (authResult) {
            userModel.addFirestoreUser()
        }
    }


}