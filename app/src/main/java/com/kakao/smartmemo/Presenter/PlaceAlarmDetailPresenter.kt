package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.PlaceAlarmDetailContract
import com.kakao.smartmemo.Model.LocationModel
import net.daum.mf.map.api.MapPOIItem

class PlaceAlarmDetailPresenter: PlaceAlarmDetailContract.Presenter, PlaceAlarmDetailContract.OnTodoDetailListener {
    private var view : PlaceAlarmDetailContract.View
    private var locationModel: LocationModel

    constructor(view: PlaceAlarmDetailContract.View){
        this.view = view
        this.locationModel = LocationModel(this)
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