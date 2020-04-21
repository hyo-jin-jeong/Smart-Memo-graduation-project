package com.kakao.smartmemo.Contract

interface TodoContract {

    interface View {

    }

    interface Presenter {
        fun getGroup()
        fun setTodoAdapterModel(adapterModel : TodoAdapterContract.Model)
        fun setTodoAdapterView(adapterView : TodoAdapterContract.View)
        fun setTodoDeleteAdapterModel(deleteAdapterModel : TodoDeleteAdapterContract.Model)
        fun setTodoDeleteAdapterView(deleteAdapterView : TodoDeleteAdapterContract.View)
    }
}