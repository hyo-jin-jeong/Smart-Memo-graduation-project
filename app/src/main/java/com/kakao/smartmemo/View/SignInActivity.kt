package com.kakao.smartmemo.View

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Contract.SignInContract
import com.kakao.smartmemo.Presenter.SignInPresenter
import kotlinx.android.synthetic.main.sign_in_page.*

class SignInActivity: AppCompatActivity(), SignInContract.View {

    lateinit var presenter : SignInContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_page)
        presenter = SignInPresenter(this)

        val donebtn = findViewById<Button>(R.id.done)
        val cancelbtn = findViewById<Button>(R.id.cancel)
        var email = findViewById<EditText>(R.id.e_mail)
        var pw = findViewById<EditText>(R.id.pw)
        var name = findViewById<EditText>(R.id.name)
        var address = findViewById<EditText>(R.id.address)

        // ~!~!~!~!~!나중에 형식 안지켰을 때 경고 띄우기 구현하기~!~!~!~!~!
        donebtn.setOnClickListener {
            if (email.text.isEmpty() || pw.text.isEmpty() || name.text.isEmpty() || address.text.isEmpty()) {
                Toast.makeText(this, "모든 빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show() // 애니메이션 넣기
            } else if(!(email.text.isEmpty() && pw.text.isEmpty() && name.text.isEmpty() && address.text.isEmpty())) {
                var email = e_mail.text.toString()
                var pw = pw.text.toString()
                var name = name.text.toString()
                var address = address.text.toString()
                Log.e("info", email)
                presenter.addUser(this, email, pw, name, address)
                finish()
            }
        }
        cancelbtn.setOnClickListener {
            finish()
        }
    }
}