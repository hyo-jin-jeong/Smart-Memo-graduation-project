package com.kakao.smartmemo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog



class MemberData :AppCompatActivity(){
    lateinit var memberToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.member_view)
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
                val member_data_change =Intent(this,MemberDataChange::class.java)
                startActivity(member_data_change)

                return true
            }

            R.id.action_settings2 -> {
                Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()

                return true
            }
            R.id.action_settings3 -> {

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
                            var builder2 = AlertDialog.Builder(this)
                            builder2.setTitle("알림")
                            builder2.setMessage("저희 서비스를 이용해 주셔서 감사합니다.")
                            builder2.show()
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
}