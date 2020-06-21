package com.kakao.smartmemo.Contract

interface MemoDialogContract {
    interface View {
        fun showMemoItem(position: Int)
    }

    interface Presenter {
        fun setMemoDialogAdapterModel(model: MemoDialogAdapterContract.Model)
        fun setMemoDialogAdapterView(view: MemoDialogAdapterContract.View)
    }
}