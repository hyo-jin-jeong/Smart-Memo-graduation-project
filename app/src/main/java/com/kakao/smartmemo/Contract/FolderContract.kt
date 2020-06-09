package com.kakao.smartmemo.Contract

interface  FolderContract{
    interface View{

    }
    interface Presenter{
        fun getMemoList()
        fun getTodoList()

    }
}