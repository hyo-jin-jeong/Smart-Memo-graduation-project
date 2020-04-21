package com.kakao.smartmemo.Contract

interface MemoAdapterContract {
    interface Model {
        fun getMemo()
        fun deleteMemo()
    }

    interface View {
        fun notifyAdapter()
    }

}
