package com.kakao.smartmemo.Model

object UserModel{
    var email:String = ""
    var password:String =""
    var user_name:String =""
    var uid:String =""
    var addr:String=""
    var kakao_conected = false
    var kakaoAlarm_time:String = ""
    lateinit var group_id:MutableList<String>
}