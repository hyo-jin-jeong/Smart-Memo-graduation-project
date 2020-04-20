package com.kakao.smartmemo.View

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Contract.SignInContract
import com.kakao.smartmemo.Presenter.SignInPresenter

class SignInActivity: AppCompatActivity(), SignInContract.View {

    lateinit var presenter : SignInContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_page)
        presenter = SignInPresenter(this)

        val donebtn = findViewById(R.id.done) as Button
        val cancelbtn = findViewById(R.id.cancel) as Button

        donebtn.setOnClickListener {
            presenter.addUser()
            finish()
        }
        cancelbtn.setOnClickListener {
            finish()
        }
    }
}