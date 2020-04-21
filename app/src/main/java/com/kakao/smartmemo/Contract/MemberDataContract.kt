package com.kakao.smartmemo.Contract

import android.widget.EditText

interface MemberDataContract {
    interface Presenter{
        fun getProfile(email:String){}
        fun updateUser(){}
        fun checkPassword(confirmPassword: EditText?) :Boolean{return true}
        fun deleteUser(){}
    }
    interface  View{

    }


}