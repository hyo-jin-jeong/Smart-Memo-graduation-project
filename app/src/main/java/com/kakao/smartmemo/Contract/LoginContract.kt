package com.kakao.smartmemo.Contract

import android.app.Activity

interface LoginContract {
    interface Presenter {
        fun checkUser(context: Activity, email:String, password:String)
        fun getProfile(email:String)
    }

    interface View {
        fun startMainActivity()
        fun onLoginSuccess(message: String)
        fun onLoginFailure(message: String)
    }

    interface onLoginListener{
        fun onSuccess(message:String)
        fun onFailure(message:String)
    }
}