package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.Contract.AddMemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Presenter.AddMemoPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.time_location_settings.view.*
import java.text.SimpleDateFormat
import java.util.*

class AddMemo : AppCompatActivity(), AddMemoContract.View {
    lateinit var memoToolbar: Toolbar
    lateinit var presenter: AddMemoPresenter
    lateinit var saveBtn :Button
    lateinit var titleEdit : EditText
    lateinit var dateText : TextView
    lateinit var contentEdit:EditText
    lateinit var groupName : String
    lateinit var placeNameText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memo)

        //Toolbar 달기
        memoToolbar = findViewById<Toolbar>(R.id.addMemoToolbar)
        setSupportActionBar(memoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        memoToolbar.title = resources.getString(R.string.write_memo)

        //presenter 초기화
        presenter = AddMemoPresenter(this)
        saveBtn = findViewById(R.id.save_button)
        titleEdit = findViewById(R.id.memo_title)
        dateText = findViewById<TextView>(R.id.memo_date)
        contentEdit = findViewById(R.id.memo_content)
        //groupName = findViewById(R.id.) ->그룹 이름 초기화
        placeNameText = findViewById(R.id.place_name)


        val date = Date(System.currentTimeMillis())
        val formatDate = SimpleDateFormat("yyyy.MM.dd")
        var today = formatDate.format(date)

        dateText.text = today
        saveBtn.setOnClickListener {
                val memoData = MemoData(titleEdit.text.toString(),today,contentEdit.text.toString(),"",placeNameText.text.toString(),0.0,0.0)
                presenter.addMemo(memoData)
                val intent = Intent(applicationContext, ShowMemo::class.java)
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