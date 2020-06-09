package com.kakao.smartmemo.View

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
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
import java.util.*

class MemberDataChange :AppCompatActivity(),MemberChangeContract.View {
    lateinit var presenter : MemberChangeContract.Presenter
    private lateinit var memberToolbar: Toolbar
    private lateinit var emailText : TextView
    private lateinit var passwordText : EditText
    private lateinit var retypePasswordText : EditText
    private lateinit var nameText : EditText
    private lateinit var addrText : EditText
    private lateinit var kakaoAlarmTimeText : TextView
    private lateinit var showPassword : ImageView
    private lateinit var hidePassword : ImageView
    private lateinit var showRetypePassword : ImageView
    private lateinit var hideRetypePassword : ImageView
    private lateinit var switchTodoAlarm: Switch
    private val calendarTodo = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_change_view)
        presenter = MemberChangePresenter(this)

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        retypePasswordText = findViewById(R.id.retype_passwordText)
        nameText = findViewById(R.id.nameText)
        addrText = findViewById(R.id.addrText)
        kakaoAlarmTimeText = findViewById(R.id.kakao_alarm_time_text)
        showPassword = findViewById(R.id.showPassword)
        hidePassword = findViewById(R.id.hidePassword)
        showRetypePassword = findViewById(R.id.showRetypePassword)
        hideRetypePassword = findViewById(R.id.hideRetypePassword)
        switchTodoAlarm = findViewById(R.id.switch_todo_alarm)

        if (UserObject != null) {
            emailText.text = UserObject.email
            passwordText.setText(UserObject.password)
            nameText.setText(UserObject.user_name)
            addrText.setText(UserObject.addr)
            if (UserObject.kakao_alarm_time != "") {
                switchTodoAlarm.isChecked = true
                kakaoAlarmTimeText.text = UserObject.kakao_alarm_time
            } else switchTodoAlarm.isChecked = false
        }

        memberToolbar = findViewById(R.id.member_toolbar)
        setSupportActionBar(memberToolbar)
        member_icon.clipToOutline = true

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        kakao_alarm_time_text.setOnClickListener {
            var listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var hour: Int
                var am_pm = "오전"
                var m = minute.toString()
                calendarTodo.timeInMillis
                if (hourOfDay == 0) { am_pm = "오전" }
                if (hourOfDay >= 12) {
                    am_pm = "오후"
                    hour = hourOfDay % 12
                    if (hour == 0) { hour = 12 }
                } else { hour = hourOfDay }
                if (minute == 0) { m = "00" }
                calendarTodo.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendarTodo.set(Calendar.MINUTE, minute)
                calendarTodo.set(Calendar.SECOND, 0)
                kakaoAlarmTimeText.text = "${am_pm} ${hour} : ${m} "
            }
            val dialog = TimePickerDialog(this, listener, 12, 0, false)

            dialog.show()
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
            retypePasswordText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showRetypePassword.visibility = View.GONE
            hideRetypePassword.visibility = View.VISIBLE
        }
        hideRetypePassword.setOnClickListener {
            retypePasswordText.transformationMethod = PasswordTransformationMethod.getInstance()
            hideRetypePassword.visibility = View.GONE
            showRetypePassword.visibility = View.VISIBLE
        }

        switchTodoAlarm.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (switchTodoAlarm.isChecked) {
                kakaoAlarmTimeText.visibility = View.VISIBLE
            } else {
                kakaoAlarmTimeText.text = ""
                kakaoAlarmTimeText.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_save -> {
                if (passwordText.text.toString() != UserObject.password) {
                    if (passwordText.text.toString() != retypePasswordText.text.toString()) {
                        Toast.makeText(
                            applicationContext,
                            "비밀번호와 비밀번호 확인이 다릅니다.\n재입력해주십시오.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        var pw = passwordText.text.toString()
                        var name = nameText.text.toString()
                        var address = addrText.text.toString()
                        var kakaoAlarmTime = kakaoAlarmTimeText.text.toString()
                        if (kakaoAlarmTime == "없음") {
                            kakaoAlarmTime = ""
                        }
                        presenter.updateUser(this, pw, name, address, kakaoAlarmTime)
                        presenter.updatePassword(pw)
                    }
                } else {
                    var pw = passwordText.text.toString()
                    var name = nameText.text.toString()
                    var address = addrText.text.toString()
                    var kakaoAlarmTime = kakaoAlarmTimeText.text.toString()
                    presenter.updateUser(this, pw, name, address, kakaoAlarmTime)
                    finish()
                }
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