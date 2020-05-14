package com.kakao.smartmemo.Contract

import android.app.Activity

interface MemberChangeContract {
    interface Presenter{
        fun getProfile(email:String){}
        fun updateUser(context: Activity, pw: String, name: String, addr: String, kakaoAlarmTime: String){}
        fun updatePassword(pw:String)
    }
    interface  View{

    }


}