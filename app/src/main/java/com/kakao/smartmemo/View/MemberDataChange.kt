package com.kakao.smartmemo.View

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Contract.MemberChangeContract
import com.kakao.smartmemo.Object.UserObject
import com.kakao.smartmemo.Presenter.MemberChangePresenter
import kotlinx.android.synthetic.main.member_change_view.*

class MemberDataChange :AppCompatActivity(),MemberChangeContract.View {
    lateinit var presenter : MemberChangeContract.Presenter
    lateinit var memberToolbar: Toolbar
    lateinit var emailText : TextView
    lateinit var passwordText : EditText
    lateinit var retype_passwordText : EditText
    lateinit var nameText : EditText
    lateinit var addrText : EditText
    lateinit var kakao_alarm_time : TextView
    lateinit var showPassword : ImageView
    lateinit var hidePassword : ImageView
    lateinit var showRetypePassword : ImageView
    lateinit var hideRetypePassword : ImageView
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_change_view)
        presenter = MemberChangePresenter(this)

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        retype_passwordText = findViewById(R.id.retype_passwordText)
        nameText = findViewById(R.id.nameText)
        addrText = findViewById(R.id.addrText)
        kakao_alarm_time = findViewById(R.id.kakao_alarm_time)
        showPassword = findViewById(R.id.showPassword)
        hidePassword = findViewById(R.id.hidePassword)
        showRetypePassword = findViewById(R.id.showRetypePassword)
        hideRetypePassword = findViewById(R.id.hideRetypePassword)
        button = findViewById(R.id.button)

        if (UserObject != null) {
            emailText.text = UserObject.email
            passwordText.setText(UserObject.password)
            nameText.setText(UserObject.user_name)
            addrText.setText(UserObject.addr)
            kakao_alarm_time.text = UserObject.kakao_alarm_time
        }

        memberToolbar = findViewById(R.id.member_toolbar)
        setSupportActionBar(memberToolbar)
        member_icon.clipToOutline = true

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        kakao_alarm_timeView.setOnClickListener {
            var listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var hour: Int
                var am_pm = "오전"
                var m = minute.toString()
                if (hourOfDay == 0) { am_pm = "오전" }
                if (hourOfDay >= 12) {
                    am_pm = "오후"
                    hour = hourOfDay % 12
                    if (hour == 0) { hour = 12 }
                } else { hour = hourOfDay }
                if (minute == 0) { m = "00" }
                kakao_alarm_time.text = "${am_pm} ${hour} : ${m} "
            }
            val dialog = TimePickerDialog(this, listener, 12, 0, false)

            dialog.show()
        }

        button.setOnClickListener{
            if (passwordText.text.toString() != UserObject.password) {
                if (passwordText.text.toString() != retype_passwordText.text.toString())
                    Toast.makeText(applicationContext, "비밀번호와 비밀번호 확인이 다릅니다.\n재입력해주십시오.", Toast.LENGTH_SHORT).show()
                else {
                    var pw = passwordText.text.toString()
                    var name = nameText.text.toString()
                    var address = addrText.text.toString()
                    var kakaoAlarmTime = kakao_alarm_time.text.toString()
                    Log.e("save 터치", "터치 성공")
                    presenter.updateUser(this, pw, name, address, kakaoAlarmTime)
                    Log.e("순서3", "저장하고 view로 돌아옴")
                    presenter.updatePassword(pw)
                }
            } else {
                var pw = passwordText.text.toString()
                var name = nameText.text.toString()
                var address = addrText.text.toString()
                var kakaoAlarmTime = kakao_alarm_time.text.toString()
                Log.e("save 터치", "터치 성공")
                presenter.updateUser(this, pw, name, address, kakaoAlarmTime)
                finish()
            }
        }

        showPassword.setOnClickListener {
            passwordText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showPassword.visibility = View.GONE
            hidePassword.visibility = View.VISIBLE
        }
        hidePassword.setOnClickListener {
            passwordText.transformationMethod = PasswordTransformationMethod.getInstance()
            hidePassword.visibility = View.GONE
            showPassword.visibility = View.VISIBLE
        }
        showRetypePassword.setOnClickListener {
            retype_passwordText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showRetypePassword.visibility = View.GONE
            hideRetypePassword.visibility = View.VISIBLE
        }
        hideRetypePassword.setOnClickListener {
            retype_passwordText.transformationMethod = PasswordTransformationMethod.getInstance()
            hideRetypePassword.visibility = View.GONE
            showRetypePassword.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSuccess() {
        finish()
        Toast.makeText(applicationContext, "비밀번호까지 수정 완료", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure() {
        Toast.makeText(applicationContext, "정보수정 실패", Toast.LENGTH_SHORT).show()
    }
}