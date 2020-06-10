package com.kakao.smartmemo.View

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.Contract.MemberDataContract
import com.kakao.smartmemo.Object.UserObject
import com.kakao.smartmemo.Presenter.MemberDataPresenter
import com.kakao.smartmemo.R

class MemberData :AppCompatActivity() , MemberDataContract.View{
    lateinit var presenter:MemberDataContract.Presenter
    lateinit var memberToolbar: Toolbar
    lateinit var userEmail : TextView
    lateinit var userName : TextView
    lateinit var userAddr : TextView
    lateinit var kakaoAlarmTime : TextView
    lateinit var changeInfo : TextView
    lateinit var memberExit : Button
    lateinit var logoutLayout : RelativeLayout
    lateinit var nothingTextView : TextView

    override fun onResume() {
        super.onResume()
        userEmail.text = UserObject.email
        userName.text = UserObject.user_name
        userAddr.text = UserObject.addr
        kakaoAlarmTime.text = UserObject.kakao_alarm_time
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_view)
        presenter = MemberDataPresenter(this)

        userEmail = findViewById(R.id.user_email)
        userName = findViewById(R.id.user_name)
        userAddr = findViewById(R.id.user_addr)
        kakaoAlarmTime = findViewById(R.id.kakao_alarm_time_text)
        changeInfo = findViewById(R.id.change_info)
        memberExit = findViewById(R.id.member_exit)
        logoutLayout = findViewById(R.id.logout_layout)
        nothingTextView = findViewById(R.id.nothing_text)
        nothingTextView.isClickable = false

        presenter.getProfile()//User 정보가져오기

        if (UserObject != null) {
            userEmail.text = UserObject.email
            userName.text = UserObject.user_name
            userAddr.text = UserObject.addr
            kakaoAlarmTime.text = UserObject.kakao_alarm_time
        } else { // null 이라면
            Toast.makeText(applicationContext, "정보를 가져오는데 실패했습니다.\n앱을 다시 시작해주세요.", Toast.LENGTH_SHORT).show()
        }

        memberToolbar = findViewById(R.id.member_toolbar)
        setSupportActionBar(memberToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        changeInfo.setOnClickListener {
            val memberDataChange = Intent(this, MemberDataChange::class.java)
            startActivity(memberDataChange)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}