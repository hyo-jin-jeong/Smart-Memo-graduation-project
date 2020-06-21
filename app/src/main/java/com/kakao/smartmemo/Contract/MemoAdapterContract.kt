package com.kakao.smartmemo.Contract

interface MemoAdapterContract {
    interface Model {
        fun getMemo(position :Int) :Int
        fun deleteMemo()
    }

    interface View{
        var onClickFunc : ((Int) ->Unit)?
        fun notifyAdapter()
    }

}
