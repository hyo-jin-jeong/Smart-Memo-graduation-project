package com.kakao.smartmemo.Contract

import net.daum.mf.map.api.MapPOIItem

interface PlaceAlarmDetailContract {
    interface Presenter {
        fun convertAddressFromMapPOIItem(mapPOIItem: MapPOIItem)
    }

    interface View {
        fun getLocationName(mapPOIItem: MapPOIItem, locationName: String?)
    }

    interface OnTodoDetailListener{
        fun onSuccess(mapPOIItem: MapPOIItem, locationName: String?)
        fun onFailure()
    }
}