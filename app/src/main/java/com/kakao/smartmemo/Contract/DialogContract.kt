package com.kakao.smartmemo.Contract

interface DialogContract {
    interface View {

    }
    interface Presenter {
        fun setDialogAdapterModel(adapterModel: DialogAdapterContract.Model)
        fun setDialogAdapterView(adapterView: DialogAdapterContract.View)
    }
}