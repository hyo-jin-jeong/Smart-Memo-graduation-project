package com.kakao.smartmemo.Contract

interface MemoDialogAdapterContract {
    interface Model {
        fun getMemo(position :Int) :Int
    }

    interface View {
        var onClickFunc : ((Int) ->Unit)?
        fun notifyAdapter()
    }

}