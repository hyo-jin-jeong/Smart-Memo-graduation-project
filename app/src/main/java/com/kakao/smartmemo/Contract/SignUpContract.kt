package com.kakao.smartmemo.Contract

import android.app.Activity

interface SignUpContract {
    interface Presenter {
        fun addUser(context: Activity, email:String, pw:String, name:String, address:String)
        fun addFirestoreUser()
    }
    interface View {
        fun onSignUpSuccess(message: String)
        fun onSignUpFailure(message: String)
    }

    interface onSignUpListener {
        fun onSuccess(message: String)
        fun onFailure(message: String)
    }

}