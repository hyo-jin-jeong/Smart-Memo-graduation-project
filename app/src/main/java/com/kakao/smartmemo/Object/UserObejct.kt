package com.kakao.smartmemo.Object

object UserObejct{
    var email:String = ""
    var password:String =""
    var user_name:String =""
    var uid:String =""
    var addr:String=""
    var profile_id:String=""
    var profile_url:String=""
    var kakao_conected = false
    var kakaoAlarm_time:String = ""
    lateinit var group_info:MutableMap<String,String>//키 : 그룹 id value: 그룹 name
}