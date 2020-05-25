package com.kakao.smartmemo.Contract

import com.kakao.smartmemo.Data.MemoData


interface MemoAdapterContract {
    interface Model {
        fun getMemo(position :Int) : MemoData
        fun deleteMemo()
    }

    interface View{
        var onClickFunc : ((Int) ->Unit)?
        fun notifyAdapter()
    }

}
