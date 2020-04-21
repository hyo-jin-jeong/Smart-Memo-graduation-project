package com.kakao.smartmemo.Contract

interface MemberChangeContract {
    interface Presenter{
        fun getProfile(email:String){}
        fun updateUser(){}
    }
    interface  View{

    }


}