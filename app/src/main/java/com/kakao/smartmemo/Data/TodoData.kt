package com.kakao.smartmemo.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoData(
    var todoId: String = "", var title: String="",
    var groupId: String,
    var setTimeAlarm: Boolean = false, var timeDate: String = "", var timeTime: String = "", var timeAgain: Int = 0,
    var setPlaceAlarm: Boolean = false, var placeDate: String = "", var placeAgain: Int = 0)
    :Parcelable
