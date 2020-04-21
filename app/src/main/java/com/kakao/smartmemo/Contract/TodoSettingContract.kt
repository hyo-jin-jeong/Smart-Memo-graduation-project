package com.kakao.smartmemo.Contract

interface TodoSettingContract {
    interface View {

    }
    interface Presenter {
        fun addTimeAlarm()
        fun addPlaceAlarm()
        fun setTodoDateAdapterModel(adapterModel : TodoDateAdapterContract.Model)
        fun setTodoDateAdapterView(adapterView : TodoDateAdapterContract.View)
        fun setTodoPlaceAdapterModel(adapterModel : TodoPlaceAdapterContract.Model)
        fun setTodoPlaceAdapterView(adapterView : TodoPlaceAdapterContract.View)
    }
}