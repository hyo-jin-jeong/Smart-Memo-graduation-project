package com.kakao.smartmemo.Contract

import android.widget.EditText

interface MemberDataContract {
    interface Presenter{
        fun getProfile(email:String){}
        fun updateUser(){}
        fun signOutUser() {}
        fun checkPassword(confirmPassword: String) :Boolean{return true}
        fun deleteUser(){}
    }
    interface  View{

    }


}