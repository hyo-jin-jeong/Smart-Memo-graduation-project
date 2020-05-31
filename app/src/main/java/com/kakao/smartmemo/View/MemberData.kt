package com.kakao.smartmemo.View

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    lateinit var user_email : TextView
    lateinit var user_name : TextView
    lateinit var user_addr : TextView
    lateinit var kakao_alarm_time : TextView

    override fun onResume() {
        super.onResume()
        user_email.text = UserObject.email
        user_name.text = UserObject.user_name
        user_addr.text = UserObject.addr
        kakao_alarm_time.text = UserObject.kakao_alarm_time
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_view)
        presenter = MemberDataPresenter(this)

        user_email = findViewById(R.id.user_email)
        user_name = findViewById(R.id.user_name)
        user_addr = findViewById(R.id.user_addr)
        kakao_alarm_time = findViewById(R.id.kakao_alarm_time)

        presenter.getProfile()//User 정보가져오기

        if (UserObject != null) {
            user_email.text = UserObject.email
            user_name.text = UserObject.user_name
            user_addr.text = UserObject.addr
            kakao_alarm_time.text = UserObject.kakao_alarm_time
        } else { // null 이라면
            Toast.makeText(applicationContext, "정보를 가져오는데 실패했습니다.\n앱을 다시 시작해주세요.", Toast.LENGTH_SHORT).show()
        }

        memberToolbar = findViewById(R.id.member_toolbar)
        setSupportActionBar(memberToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.member_menu, menu)
        menu?.getItem(1)?.isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_settings1 -> {
                val memberDataChange = Intent(this, MemberDataChange::class.java)
                startActivity(memberDataChange)
                return true
            }

            R.id.action_settings2 -> { //로그아웃
                presenter.signOutUser()
                var intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(this, "로그아웃에 성공! 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings3 -> { //탈퇴
                var builder = AlertDialog.Builder(this)
                var builder2 = AlertDialog.Builder(this)
                builder2.setTitle("알림")
                builder2.setMessage("저희 서비스를 이용해 주셔서 감사합니다.")

                builder.setTitle("알림")
                builder.setMessage("탈퇴하시겠습니까?")
                builder.setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    var builder1 = AlertDialog.Builder(this)
                    builder1.setTitle("알림")
                    //builder1.setMessage("비밀번호를 입력하시오")
                    var typePassword = EditText(this)
//                    typePassword.hint = "비밀번호를 입력하시오"

                    typePassword.setPadding(10,5,10,0)
                    builder1.setView(typePassword)
                    typePassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    builder1.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
                        when {
                            typePassword.text.isBlank() -> Toast.makeText(this, "비밀번호를 입력하시오", Toast.LENGTH_SHORT).show()
                            presenter.checkPassword(typePassword.text.toString()) -> {
                                presenter.deleteUser()
                                //builder2.show()
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                val mHandler = Handler()
                                mHandler.postDelayed(Runnable {
                                    startActivity(intent)
                                }, 1000)
                            }
                            else -> {
                                Toast.makeText(this, "비밀번호가 틀렸습니다.\n다시 입력해주십시오", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                    builder1.setNegativeButton(
                        "아니오",
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->

                        })
                    builder1.show()
                })
                builder.setNegativeButton("아니오",DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                })

                builder.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onReStartApp() {

    }
}