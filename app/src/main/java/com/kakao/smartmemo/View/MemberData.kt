package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.kakao.smartmemo.Contract.MemberDataContract
import com.kakao.smartmemo.Object.UserObejct
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Presenter.MemberDataPresenter
import kotlinx.android.synthetic.main.secession_popup.*

class MemberData :AppCompatActivity() , MemberDataContract.View{
    lateinit var presenter:MemberDataContract.Presenter
    lateinit var memberToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_view)
        presenter = MemberDataPresenter(this)

        presenter.getProfile()//User 정보가져오기

        memberToolbar = findViewById(R.id.member_toolbar)
        setSupportActionBar(memberToolbar)

        //앱 이름 없애는-
        getSupportActionBar()?.setDisplayShowTitleEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

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
                val member_data_change =Intent(this,
                    MemberDataChange::class.java)
                    startActivity(member_data_change)

                return true
            }

            R.id.action_settings2 -> {//로그아웃
                initUserObject()
                return true
            }
            R.id.action_settings3 -> {//탈퇴

                var builder = AlertDialog.Builder(this)

                builder.setTitle("알림")
                builder.setMessage("탈퇴하시겠습니까?")
                builder.setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->

                    var builder1 = AlertDialog.Builder(this)
                    builder1.setTitle("알림")
                    builder1.setView(R.layout.secession_popup)
                    builder1.setPositiveButton(
                        "예",
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                            if(presenter.checkPassword(confirm_password)){
                                var builder2 = AlertDialog.Builder(this)
                                builder2.setTitle("알림")
                                builder2.setMessage("저희 서비스를 이용해 주셔서 감사합니다.")
                                builder2.show()
                                presenter.deleteUser()
                                initUserObject()

                            }
                            else{
                                confirm_password.hint="비밀번호를 다시 입력하시오"
                            }

                        })
                    builder1.setNegativeButton(
                        "아니오",
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                            Toast.makeText(applicationContext, "아니오를 선택했습니다.", Toast.LENGTH_LONG)
                                .show()
                        })
                    builder1.show()
                })
                builder.setNegativeButton("아니오",DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(applicationContext,"아니오를 선택했습니다.",Toast.LENGTH_LONG).show()
                })

                builder.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun initUserObject(){
        with(UserObejct){
            email=""
            password=""
            uid=""
            addr=""
            user_name=""
            profile_id=""
            profile_url=""
            kakao_conected=false
            kakaoAlarm_time=""
            group_info = mutableMapOf("" to "")
        }
        var intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}