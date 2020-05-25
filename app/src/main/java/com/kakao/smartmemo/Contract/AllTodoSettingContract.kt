package com.kakao.smartmemo.Contract

interface AllTodoSettingContract {
    interface Presenter {
        fun setTodoDateAdapterModel(adapterModel : TodoDateAdapterContract.Model)
        fun setTodoDateAdapterView(adapterView : TodoDateAdapterContract.View)
        fun setTodoPlaceAdapterModel(adapterModel : TodoPlaceAdapterContract.Model)
        fun setTodoPlaceAdapterView(adapterView : TodoPlaceAdapterContract.View)
    }
    interface View {

    }
}