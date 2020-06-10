package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.PlaceData

interface TodoPlaceAdapterContract {

    interface Model {
        fun getTodoPlace()
        fun deletePlace()
        fun getList() : ArrayList<PlaceData>
    }

    interface View {
        fun notifyAdapter()
    }
}