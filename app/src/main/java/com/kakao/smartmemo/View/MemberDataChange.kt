package com.kakao.smartmemo.View

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.Contract.MemberChangeContract
import com.kakao.smartmemo.Object.UserObject
import com.kakao.smartmemo.Presenter.MemberChangePresenter
import com.kakao.smartmemo.R
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
    lateinit var memberExit : Button
    lateinit var logoutLayout : RelativeLayout
    lateinit var nothingTextView : TextView
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
        memberExit = findViewById(R.id.member_exit)
        logoutLayout = findViewById(R.id.logout_layout)
        nothingTextView = findViewById(R.id.nothing_text)
        nothingTextView.isClickable = false

        if (UserObject != null) {
            emailText.text = UserObject.email
            passwordText.setText(UserObject.password)
            nameText.setText(UserObject.user_name)
            addrText.setText(UserObject.addr)
            if (UserObject.kakao_alarm_time != "") {
                switchTodoAlarm.isChecked = true
                kakaoAlarmTimeText.visibility = View.VISIBLE
                kakaoAlarmTimeText.text = UserObject.kakao_alarm_time
            } else  {
                switchTodoAlarm.isChecked = false
                kakaoAlarmTimeText.visibility = View.GONE
            }
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
                if (UserObject.kakao_alarm_time == "") {
                    kakaoAlarmTimeText.text = "시간 설정 안함"
                } else {
                    kakaoAlarmTimeText.text = UserObject.kakao_alarm_time
                }
            } else {
                kakaoAlarmTimeText.visibility = View.GONE
            }
        }

        logoutLayout.setOnClickListener {
            var logoutBuilder = AlertDialog.Builder(this)
            logoutBuilder.setTitle("로그아웃")
            logoutBuilder.setMessage("정말 로그아웃을 하시겠습니까?")
            logoutBuilder.setPositiveButton("로그아웃", DialogInterface.OnClickListener { dialog, which ->
                presenter.signOutUser()
                var intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(this, "로그아웃에 성공! 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
            })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                .create()
                .show()
        }

        memberExit.setOnClickListener {
            var checkExitBuilder = AlertDialog.Builder(this)
                .setTitle("회원 탈퇴")
                .setMessage("탈퇴하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { checkExitDialog, i ->
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view = inflater.inflate(R.layout.alert_dialog, null)
                    var editText = view.findViewById<EditText>(R.id.type_password)
                    var typePasswordBuilder = AlertDialog.Builder(this)
                        .setView(view)
                        .setTitle("알림")
                        .setPositiveButton("예", DialogInterface.OnClickListener { typePasswordDialog, i ->
                            if (editText.text.isEmpty()) {
                                Toast.makeText(this, "비밀번호를 입력하시오", Toast.LENGTH_SHORT).show()
                            } else {
                                if (presenter.checkPassword(editText.text.toString())) {
                                    presenter.deleteUser()
                                    var finishBuilder = AlertDialog.Builder(this)
                                        .setTitle("알림")
                                        .setMessage("저희 서비스를 이용해 주셔서 감사합니다.")
                                        .create()

                                    val intent = Intent(this, LoginActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    val mHandler = Handler()
                                    mHandler.postDelayed(Runnable {
                                        startActivity(intent)
                                        typePasswordDialog.dismiss()
                                        finishBuilder.show()
                                    }, 1000)
                                } else {
                                    Toast.makeText(this, "비밀번호가 틀렸습니다.\n다시 입력해주십시오", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                        .setNegativeButton("아니오", DialogInterface.OnClickListener() { dialog, which ->  dialog.cancel() })
                        .create()
                        .show() })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                .create()
                .show()
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
                        if (kakaoAlarmTime == "시간 설정 안함") {
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
                    if (kakaoAlarmTime == "시간 설정 안함") {
                        kakaoAlarmTime = ""
                    }
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