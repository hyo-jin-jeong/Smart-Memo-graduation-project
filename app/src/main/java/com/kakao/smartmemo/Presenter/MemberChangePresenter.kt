package com.kakao.smartmemo.Presenter

import android.app.Activity
import com.kakao.smartmemo.Contract.MemberChangeContract
import com.kakao.smartmemo.Model.UserModel
import com.kakao.smartmemo.Object.UserObject

class MemberChangePresenter : MemberChangeContract.Presenter{
    private var view : MemberChangeContract.View
    var userModel: UserModel

    constructor(view:MemberChangeContract.View){
        this.view= view
        this.userModel = UserModel()
    }

    override fun getProfile(email:String) {
        userModel.getProfile(email)
    }

    override fun updateUser(context: Activity, pw: String, name: String, addr: String, kakaoAlarmTime: String) {
        userModel.addFirestoreUser(pw, name, addr, kakaoAlarmTime)
    }

    override fun updatePassword(pw: String) {
        userModel.updateUserPassword(pw)
    }
}