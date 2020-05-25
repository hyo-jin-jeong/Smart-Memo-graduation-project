package com.kakao.smartmemo.Contract

interface MemoDeleteAdapterContract {
    interface Model {
        fun getMemo()
        fun deleteMemo()
    }

    interface View {
        fun notifyAdapter()
    }

}
