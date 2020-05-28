package com.kakao.smartmemo.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemoData(
    var title:String = "", var date:String = "", var content:String= "", var groupName:String= "",
    var groupId:String= "",var groupColor:Long = 0,
    var placeName:String= "", var latitude:Double=0.0, var longitude:Double=0.0
)//memo list item data
    : Parcelable

