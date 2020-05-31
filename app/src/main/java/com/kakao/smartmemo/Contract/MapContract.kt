package com.kakao.smartmemo.Contract

import net.daum.mf.map.api.MapPOIItem

interface MapContract {
    interface View {
        fun getLocationName(mapPOIItem: MapPOIItem, locationName: String?)
    }
    interface Presenter{
        fun convertAddressFromMapPOIItem(mapPOIItem: MapPOIItem)
        fun getMemo()
        fun getTodoPlaceAlarm()
    }
    interface OnMapListener{
        fun onSuccess(mapPOIItem: MapPOIItem, locationName: String?)
        fun onFailer()
    }
}