package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.kakao.smartmemo.Contract.LoginContract
import com.kakao.smartmemo.Object.UserObejct
import com.kakao.smartmemo.Presenter.LoginPresenter
import com.kakao.smartmemo.R

class LoginActivity: AppCompatActivity(), LoginContract.View {
    private lateinit var id: EditText
    private lateinit var pw: EditText
    lateinit var presenter : LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        presenter = LoginPresenter(this)

        val loginButton = findViewById<CardView>(R.id.login_button)
        id = findViewById<EditText>(R.id.id)
        pw = findViewById<EditText>(R.id.pw)
        loginButton.setOnClickListener {
            if (id.text.toString() != "" && pw.text.toString() != "")
                presenter.checkUser(this, id.text.toString(), pw.text.toString())
            else {
                Toast.makeText(this, "다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                // 나중에 애니메이션 넣기
            }
            startActivity(Intent(this, MainActivity::class.java))
        }

        val searchInfoButton = findViewById<Button>(R.id.search_id_pw)
        searchInfoButton.setOnClickListener {
            Toast.makeText(applicationContext, "찾기 클릭", Toast.LENGTH_SHORT)
        }
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}