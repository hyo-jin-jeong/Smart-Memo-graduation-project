package com.kakao.smartmemo.Presenter

import com.kakao.smartmemo.Data.PlaceAlarmData

class AlarmAdapterPresenter {

    var view : View? = null
    var data:MutableList<PlaceAlarmData> = mutableListOf(PlaceAlarmData("한성대학교", "2020.03.14", "도서관 책 "), PlaceAlarmData("녹십자약국", "2020.03.15", "마스크 사기"))

    fun onDataChange(dataItems: MutableList<PlaceAlarmData>) {
        data.forEach {
            if(!dataItems.contains(it)){
                dataItems.add(it)
            }
        }
        view?.notifyAdapter()
    }

    fun getCount(): Int = data.size

    fun getItemAt(position: Int) = data[position]

    infix fun itemAtPosition(position: Int): PlaceAlarmData = data[position]

    interface Model {

    }

    interface View {
        fun notifyAdapter()
    }

}