package com.kakao.smartmemo.Contract

interface MemberDataContract {
    interface Presenter{
        fun getProfile(){}
        fun signOutUser() {}
        fun checkPassword(confirmPassword: String) :Boolean{return true}
        fun deleteUser(){}
    }
    interface  View{

    }
}