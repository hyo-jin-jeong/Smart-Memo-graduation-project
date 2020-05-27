package com.kakao.smartmemo.Data

data class TodoData(
    var title: String, var groupName: String = "", var timeAlarmId: String = "", var setTimeAlarm: Boolean = false, var timeDay: String = "", var timeDate: String = "", var timeTime: String = "", var timeAgain: String = ""
    , var placeAlarmId: String = "", var setPlaceAlarm: Boolean = false, var placeDate: String = "", var placeAgain: String = "", var placeName: String = "", var latitude: Double = 0.0, var longitude: Double = 0.0)