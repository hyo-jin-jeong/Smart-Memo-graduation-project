package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Contract.MapContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Model.LocationModel
import com.kakao.smartmemo.Model.MemoModel
import com.kakao.smartmemo.Model.TodoModel
import net.daum.mf.map.api.MapPOIItem

class MapPresenter: MapContract.Presenter, MapContract.OnMapListener, MapContract.OnPlaceListener {

    private var view: MapContract.View
    private var locationModel: LocationModel
    private var todoModel : TodoModel
    private var memoModel : MemoModel

    constructor(view: MapContract.View) {
        this.view = view
        this.locationModel = LocationModel(this)
        this.todoModel = TodoModel(this)
        this.memoModel = MemoModel(this)
    }

    override fun getMemo() {
        memoModel.getPlaceMemo()
    }

    override fun getTodoPlaceAlarm(status: String) {
        todoModel.getPlaceTodo(status)
    }

    override fun onMapSuccess(mapPOIItem: MapPOIItem, locationName: String?) {
        view.getLocationName(mapPOIItem, locationName)
    }

    override fun onMapFailure() { }

    override fun convertAddressFromMapPOIItem(mapPOIItem: MapPOIItem) {
        locationModel.convertAddressFromMapPOIItem(mapPOIItem)
    }

    override fun onPlaceSuccess(placeList: MutableList<PlaceData>, status: String) {
        view.onSuccess(placeList, status)
    }

    override fun onPlaceFailure() { }
}