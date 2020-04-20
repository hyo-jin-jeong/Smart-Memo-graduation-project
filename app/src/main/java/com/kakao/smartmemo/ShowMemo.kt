package com.kakao.smartmemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_show_memo.*

class ShowMemo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_memo)

        val memoToolbar = findViewById<Toolbar>(R.id.showMemoToolbar)
        memoToolbar.title = resources.getString(R.string.nav_my_memo)
        setSupportActionBar(memoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        writing_location.text = intent.getStringExtra("date")
        show_title.text = intent.getStringExtra("title")
        show_content.text = intent.getStringExtra("content")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_options_in_showmemo, menu)
        return true
    }

    // 뒤로가기 버튼 누르면 이전 액티비티로 돌아가는 것을 판단해주는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_modification -> {
                Toast.makeText(applicationContext, "수정하기", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_deletion -> {
                Toast.makeText(applicationContext, "삭제하기", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}