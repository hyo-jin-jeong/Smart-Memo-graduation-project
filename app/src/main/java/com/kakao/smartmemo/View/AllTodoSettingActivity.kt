package com.kakao.smartmemo.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.smartmemo.Contract.AllTodoSettingContract
import com.kakao.smartmemo.Presenter.AllTodoSettingPresenter
import com.kakao.smartmemo.R

class AllTodoSettingActivity: AppCompatActivity(), AllTodoSettingContract.View {
    lateinit var presenter : AllTodoSettingContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_settings)
        presenter = AllTodoSettingPresenter(this)
    }

}