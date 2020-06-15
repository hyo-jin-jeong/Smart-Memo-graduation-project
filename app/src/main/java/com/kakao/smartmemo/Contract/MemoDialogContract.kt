package com.kakao.smartmemo.Contract

interface MemoDialogContract {
    interface View {
        fun showMemoItem(position: Int)
    }

    interface Presenter {
        fun setMemoDialogAdatperModel(model: MemoDialogAdapterContract.Model)
        fun setMemoDialogAdatperView(view: MemoDialogAdapterContract.View)
    }
}