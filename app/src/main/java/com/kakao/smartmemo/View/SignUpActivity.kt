package com.kakao.smartmemo.View

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.smartmemo.Contract.SignUpContract
import com.kakao.smartmemo.Presenter.SignUpPresenter
import com.kakao.smartmemo.R

class SignUpActivity: AppCompatActivity(), SignUpContract.View {

    lateinit var presenter : SignUpContract.Presenter
    private lateinit var signUpDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)
        presenter = SignUpPresenter(this)
        signUpDialog = ProgressDialog(this)

        val doneBtn = findViewById<Button>(R.id.done)
        val cancelBtn = findViewById<Button>(R.id.cancel)
        var email = findViewById<EditText>(R.id.e_mail)
        var pw = findViewById<EditText>(R.id.pw)
        var name = findViewById<EditText>(R.id.name)
        var address = findViewById<EditText>(R.id.address)

        // ~!~!~!~!~!나중에 형식 안지켰을 때 경고 띄우기 구현하기~!~!~!~!~!
        doneBtn.setOnClickListener {
            if (email.text.isEmpty() || pw.text.isEmpty() || name.text.isEmpty() || address.text.isEmpty()) {
                Toast.makeText(this, "모든 빈 칸을 채워주세요.", Toast.LENGTH_SHORT).show() // 애니메이션 넣기
            } else if(!(email.text.isEmpty() && pw.text.isEmpty() && name.text.isEmpty() && address.text.isEmpty())) {
                signUpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                signUpDialog.setMessage("회원가입 하는 중")
                signUpDialog.show()
                var email = email.text.toString().trim()
                var pw = pw.text.toString().trim()
                var name = name.text.toString().trim()
                var address = address.text.toString().trim()
                presenter.addUser(this, email, pw, name, address)
            }
        }
        cancelBtn.setOnClickListener {
            finish()
        }
    }

    override fun onSignUpSuccess(message: String) {
        presenter.addFirebaseUser()
        signUpDialog.dismiss()
        finish()
    }

    override fun onSignUpFailure(message: String) {
        signUpDialog.dismiss()
        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
    }
}