package com.kakao.smartmemo.Contract

import android.app.Activity

interface MemberChangeContract {
    interface Presenter{
        fun getProfile(){}
        fun updateUser(context: Activity, pw: String, name: String, addr: String, kakaoAlarmTime: String){}
        fun updatePassword(pw:String)
        fun signOutUser() {}
        fun checkPassword(confirmPassword: String) :Boolean{return true}
        fun deleteUser(){}
    }
    interface  View{
        fun onSuccess()
        fun onFailure()
    }

    interface OnPasswordChangeSuccessListener {
        fun onSuccess()
        fun onFailure()
    }

}