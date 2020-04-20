package com.kakao.smartmemo.Model

import android.widget.EditText
import com.kakao.smartmemo.Presenter.MemberDataPresenter

class UserModel{
    lateinit var presenter:MemberDataPresenter
    fun getProfile(){

    }
    fun addUser() {

    }
    fun updateUser() {

    }
    fun deleteUser() {

    }
    fun checkUser() {
        //firebase작업
        //userObject에 데이터 세팅
    }
    fun checkPassword(confirmPassword: EditText?):Boolean {

        return true
    }



}