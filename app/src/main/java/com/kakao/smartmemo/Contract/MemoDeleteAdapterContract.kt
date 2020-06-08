package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.MemoData

interface MemoDeleteAdapterContract {
    interface Model {
        fun getMemo()
        fun deleteMemo() : MutableList<MemoData>
    }

    interface View {
        fun notifyAdapter()
    }

}
