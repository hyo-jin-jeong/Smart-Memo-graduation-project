package com.kakao.smartmemo.Data

data class MemoData(
    var title:String = "", var date:String = "", var content:String= "", var groupName:String= "",
    var groupId:String= "",
    var placeName:String= "", var latitude:Double=0.0, var longitude:Double=0.0
)//memo list item data

