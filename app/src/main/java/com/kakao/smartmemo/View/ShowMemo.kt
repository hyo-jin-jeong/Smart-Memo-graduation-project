package com.kakao.smartmemo.View

import android.app.Activity
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
import com.kakao.smartmemo.Object.FolderObject
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
    private var groupId = ""
    private var memoId = ""
    private val REQUEST_TEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_memo)

        presenter = ShowMemoPresenter(this)
        memoToolbar = findViewById(R.id.show_memo_toolbar)
        titleText = findViewById(R.id.show_title)
        contentText = findViewById(R.id.show_content)
        placeText = findViewById(R.id.location_name)
        dateText = findViewById(R.id.memo_date)
        if(intent.hasExtra("memoData")) {
            memoData = intent.getParcelableExtra("memoData")
            setData()
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
                var intent = Intent(this,AddMemo::class.java)
                intent.putExtra("memoData",memoData)
                startActivityForResult(intent,REQUEST_TEST)
                return true
            }
            R.id.action_deletion -> {
                Toast.makeText(applicationContext, "삭제하기", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_TEST -> {
                    if (intent != null) {
                        onResume()
                        memoData = intent.getParcelableExtra("memoData")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        memoToolbar.title = FolderObject.folderInfo[this.memoData.groupId]
        titleText.text = this.memoData.title
        contentText.text = this.memoData.content
        placeText.text = this.memoData.placeName
        dateText.text = this.memoData.date
        groupId = this.memoData.groupId
        memoId = this.memoData.memoId
        FolderObject.folderColor[groupId]?.toInt()?.let { show_memo_layout.setBackgroundColor(it) }
    }
}