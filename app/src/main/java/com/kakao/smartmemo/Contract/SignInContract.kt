package com.kakao.smartmemo.Contract

import android.app.Activity

interface SignInContract {
    interface Presenter {
        fun addUser(context: Activity, email:String, pw:String, name:String, address:String){}
    }
    interface View {

    }

}