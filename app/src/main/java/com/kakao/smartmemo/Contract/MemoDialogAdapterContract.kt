package com.kakao.smartmemo.Contract

interface MemoDialogAdapterContract {
    interface Model {
        fun getMemo()
    }

    interface View {
        fun notifyAdapter()
    }

}
