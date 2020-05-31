package com.kakao.smartmemo.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoData(
    var title: String, var groupName: String = "", var groupId: String, var groupColor: Long = 0, var timeAlarmId: String = "", var setTimeAlarm: Boolean = false, var timeDate: String = "", var timeTime: String = "", var timeAgain: Int = 0
    , var placeAlarmId: String = "", var setPlaceAlarm: Boolean = false, var placeDate: String = "", var placeAgain: Int = 0, var placeName: String = "", var latitude: String = "0.0", var longitude: String = "0.0")
    :Parcelable
