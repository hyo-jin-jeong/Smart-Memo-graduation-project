package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.PlaceData
import net.daum.mf.map.api.MapPOIItem

interface MapContract {
    interface View {
        fun getLocationName(mapPOIItem: MapPOIItem, locationName: String?)
        fun onSuccess(
            placeList: MutableList<PlaceData>,
            status: String
        )
    }
    interface Presenter{
        fun convertAddressFromMapPOIItem(mapPOIItem: MapPOIItem)
        fun getMemo()
        fun getTodoPlaceAlarm(status: String)
    }
    interface OnMapListener{
        fun onMapSuccess(mapPOIItem: MapPOIItem, locationName: String?)
        fun onMapFailure()
    }
    interface OnPlaceListener {
        fun onPlaceSuccess(placeList: MutableList<PlaceData>, status: String)
        fun onPlaceFailure()
    }

}