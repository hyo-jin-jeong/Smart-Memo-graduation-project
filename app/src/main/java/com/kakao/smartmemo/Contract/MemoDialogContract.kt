package com.kakao.smartmemo.Contract

interface MemoDialogContract {
    interface View {

    }

    interface Presenter {
        fun setMemoDialogAdatperModel(model: MemoDialogAdapterContract.Model)
        fun setMemoDialogAdatperView(view: MemoDialogAdapterContract.View)
    }
}