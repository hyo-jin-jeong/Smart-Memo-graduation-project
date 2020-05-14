package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.Contract.AddMemoContract
import com.kakao.smartmemo.Presenter.AddMemoPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.activity_add_memo.*
import java.text.SimpleDateFormat
import java.util.*

class AddMemo : AppCompatActivity(), AddMemoContract.View {
    lateinit var presenter: AddMemoPresenter
    lateinit var spinner:Spinner
    lateinit var memoTitle:String
    lateinit var memoContent:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memo)

        presenter = AddMemoPresenter(this)

        val memoToolbar = findViewById<Toolbar>(R.id.addMemoToolbar)
        val date = Date(System.currentTimeMillis())
        val formatDate = SimpleDateFormat("yyyy.MM.dd")
        var today = formatDate.format(date)

        val dateText = findViewById<TextView>(R.id.memo_date)
        dateText.text = today
        memoToolbar.title = resources.getString(R.string.write_memo)
        setSupportActionBar(memoToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinner = select_group
        var groupAdapter = ArrayAdapter.createFromResource(applicationContext,
            R.array.group, android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = groupAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != 0) {
                    memo_group.text = "[그룹] " + spinner.getItemAtPosition(position) as String
                } else {
                    memo_group.text = "[그룹 선택]"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {   }
        }

        val saveBtn = save_button
        memoTitle = memo_title.text.toString()
        memoContent = memo_content.text.toString()

        saveBtn.setOnClickListener {
                val intent = Intent(applicationContext, ShowMemo::class.java)
                intent.putExtra("date", today)
                intent.putExtra("title", memoTitle)
                intent.putExtra("content", memoContent)
                startActivity(intent)
        }

    }

    // 뒤로가기 버튼 누르면 이전 액티비티로 돌아가는 것을 판단해주는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}