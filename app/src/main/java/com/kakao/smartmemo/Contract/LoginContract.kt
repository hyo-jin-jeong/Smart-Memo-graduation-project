package com.kakao.smartmemo.Contract

import android.app.Activity

interface LoginContract {
    interface Presenter {
        fun checkUser(context: Activity, email:String, password:String)
        fun getProfile()
    }

    interface View {
        fun startMainActivity()
        fun onLoginSuccess(message: String)
        fun onLoginFailure(message: String)
    }

    interface OnLoginListener{
        fun onSuccess(message:String)
        fun onFailure(message:String)
    }
}