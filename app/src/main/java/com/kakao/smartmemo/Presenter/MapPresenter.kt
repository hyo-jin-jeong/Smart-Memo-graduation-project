package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MapContract
import com.kakao.smartmemo.Model.LocationModel
import net.daum.mf.map.api.MapPOIItem

class MapPresenter: MapContract.Presenter, MapContract.OnMapListener {

    private var view: MapContract.View
    private var locationModel: LocationModel

    constructor(view: MapContract.View) {
        this.view = view
        this.locationModel = LocationModel(this)
    }

    override fun getMemo() {
    }

    override fun getTodoPlaceAlarm() {
    }

    override fun onSuccess(mapPOIItem: MapPOIItem, locationName: String?) {
        view.getLocationName(mapPOIItem, locationName)
    }

    override fun onFailer() {

    }

    override fun convertAddressFromMapPOIItem(mapPOIItem: MapPOIItem) {
        locationModel.convertAddressFromMapPOIItem(mapPOIItem)
    }
}