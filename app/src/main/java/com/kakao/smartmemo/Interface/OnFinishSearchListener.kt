package com.kakao.smartmemo.Interface

import com.kakao.smartmemo.Data.LocationData


interface OnFinishSearchListener {
    fun onSuccess(itemList: List<LocationData>)
    fun onFail()
}