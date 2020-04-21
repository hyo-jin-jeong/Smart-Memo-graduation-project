package com.kakao.smartmemo.Contract

import android.app.Activity


interface LoginContract {
    interface Presenter {
        //fun addUser()
        fun checkUser(context: Activity, email:String, password:String)


    }
    interface View {

    }
}