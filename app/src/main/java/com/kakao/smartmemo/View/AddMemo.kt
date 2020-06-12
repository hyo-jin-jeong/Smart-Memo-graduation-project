package com.kakao.smartmemo.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kakao.smartmemo.Contract.AddMemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Presenter.AddMemoPresenter
import com.kakao.smartmemo.R
import java.text.SimpleDateFormat
import java.util.*

class AddMemo : AppCompatActivity(), AddMemoContract.View {
    lateinit var presenter: AddMemoPresenter
    private lateinit var memoToolbar: Toolbar
    private lateinit var saveBtn: Button
    private lateinit var titleEdit: EditText
    private lateinit var dateText: TextView
    private lateinit var contentEdit: EditText
    private lateinit var selectGroupBtn: Button
    private lateinit var groupName: TextView
    private lateinit var placeNameText: TextView
    private lateinit var memoData: MemoData
    private lateinit var placeData: PlaceData
    private var groupId = ""
    private var originGroupId = ""
    private var memoId = (System.currentTimeMillis() * 3000).toInt().toString()
    private var placeId = (System.currentTimeMillis() * 6000).toInt().toString()
    private var hasData = false
    private var groupCheck = false

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
        dateText = findViewById(R.id.memo_date)
        contentEdit = findViewById(R.id.memo_content)
        selectGroupBtn = findViewById(R.id.select_group)
        groupName = findViewById(R.id.memo_group)
        placeNameText = findViewById(R.id.place_name)


        val date = Date(System.currentTimeMillis())
        val formatDate = SimpleDateFormat("yyyy.MM.dd")
        var today = formatDate.format(date)

        dateText.text = today

        if (intent.hasExtra("memoData")) {
            hasData = true
            this.memoData = intent.getParcelableExtra("memoData")
            titleEdit.setText(this.memoData.title)
            contentEdit.setText(this.memoData.content)
            groupName.text = FolderObject.folderInfo[this.memoData.groupId]
            originGroupId = this.memoData.groupId
            memoId = this.memoData.memoId
            placeId = this.memoData.placeId

        }

        if (intent.hasExtra("placeData")) {
            placeData = intent.getParcelableExtra("placeData")
            placeNameText.text = placeData.place
        }

        saveBtn.setOnClickListener {
            if (groupName.text == "[그룹 선택]" || contentEdit.text.toString() == "") {
                if (groupName.text == "[그룹 선택]") {
                    Toast.makeText(this, "그룹을 선택하세요!", Toast.LENGTH_SHORT).show()
                } else if (contentEdit.text.toString() == "") {
                    Toast.makeText(this, "내용을 입력하세요!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "그룹을 선택하고, 내용을입력하세요!", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (groupCheck){
                    if(hasData) {
                        presenter.deleteMemoInfo(originGroupId,memoId)
                    }
                    originGroupId = groupId
                }//그룹이 바뀌면 저장되었던 그룹에서 memo정보를 지워야한다.
                memoData = MemoData(
                    memoId,
                    titleEdit.text.toString(),
                    today,
                    contentEdit.text.toString(),
                    originGroupId,
                    placeId,
                    placeData.place,
                    placeData.latitude.toString(),
                    placeData.longitude.toString()
                )

                presenter.addMemo(memoData)
                if (hasData) {
                    hasData = false
                    var intent = Intent()
                    intent.putExtra("memoData", memoData)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    setResult(RESULT_OK, intent)
                }

                finish()


        }
    }


        selectGroupBtn.setOnClickListener {
            selectGroup()
        }
    }


    // 뒤로가기 버튼 누르면 이전 액티비티로 돌아가는 것을 판단해주는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectGroup(){
        var i = 0
        val items:Array<CharSequence> = Array(FolderObject.folderInfo.size) {""}
        var groupIdList = Array(FolderObject.folderInfo.size){""}

        FolderObject.folderInfo.forEach {
            groupIdList[i] = it.key
            items[i] = it.value
            i++
        }

        val listDialog: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )

        listDialog.setTitle("그룹 선택")
            .setItems(items, DialogInterface.OnClickListener { _, which ->
                groupCheck = true
                groupName.text = items[which].toString()
                groupId = groupIdList[which]
            })
            .show()
    }

}