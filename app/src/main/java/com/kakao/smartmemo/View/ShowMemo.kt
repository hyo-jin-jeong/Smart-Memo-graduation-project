package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.Contract.ShowMemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Presenter.ShowMemoPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.activity_show_memo.*

class ShowMemo : AppCompatActivity(), ShowMemoContract.View {
    private lateinit var presenter: ShowMemoPresenter
    private lateinit var memoToolbar : Toolbar
    private lateinit var titleText : TextView
    private lateinit var contentText : TextView
    private lateinit var placeText : TextView
    private lateinit var dateText : TextView
    private lateinit var memoData : MemoData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_memo)

        presenter = ShowMemoPresenter(this)
        memoToolbar = findViewById(R.id.show_memo_toolbar)
        titleText = findViewById(R.id.show_title)
        contentText = findViewById(R.id.show_content)
        placeText = findViewById(R.id.location_name)
        dateText = findViewById(R.id.memo_date)
        if(intent.hasExtra("memoData")){
            memoData = intent.getParcelableExtra("memoData")
            memoToolbar.title = memoData.groupName
            titleText.text = memoData.title
            contentText.text = memoData.content
            placeText.text = memoData.placeName
            dateText.text = memoData.date
            show_memo_layout.setBackgroundColor(memoData.groupColor.toInt())
        }



        setSupportActionBar(memoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_options_in_showmemo, menu)
        return true
    }

    // 뒤로가기 버튼 누르면 이전 액티비티로 돌아가는 것을 판단해주는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_modification -> {
                var intent = Intent(this,AddGroup::class.java)
                intent.putExtra("memoData",memoData)
                startActivity(intent)
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