package com.kakao.smartmemo.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Contract.ModifyGroupContract
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Presenter.ModifyFolderPresenter

class ModifyFolder : AppCompatActivity(), ColorPickerDialogListener, ModifyGroupContract.View{

    lateinit var presenter: ModifyGroupContract.Presenter

    lateinit var toolbar: Toolbar
    lateinit var groupNameEdit : EditText
    lateinit var groupNameText :  TextView
    lateinit var themeColor : View
    lateinit var groupMemberSet:TextView
    lateinit var colorPicker: ImageView
    lateinit var saveBtn: Button
    lateinit var groupExitBtn : Button
    lateinit var kakaoImg : ImageView
    lateinit var kakaoText : TextView
    lateinit var groupId : String

    private var count = 0
    private var color = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_add_group)

        presenter = ModifyFolderPresenter(this)

        toolbar= findViewById(R.id.addGroupToolbar)
        toolbar.title = resources.getString(R.string.setting_group)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        groupNameEdit = findViewById(R.id.editGroupName)
        groupNameText = findViewById(R.id.textGroupName)
        themeColor = findViewById(R.id.selected_color)
        colorPicker = findViewById(R.id.color_picker)
        saveBtn = findViewById(R.id.save_group)
        groupMemberSet = findViewById(R.id.group_invitation)
        groupExitBtn = findViewById(R.id.member_exit)
        kakaoImg = findViewById(R.id.kakao_icon)
        kakaoText = findViewById(R.id.kakao_text)

        if (intent.hasExtra("groupId")) {
            groupId = intent.getStringExtra("groupId")
            groupNameEdit.setText(FolderObject.folderInfo[groupId])
            groupNameText.text = FolderObject.folderInfo[groupId].toString()
            FolderObject.folderColor[groupId]?.toInt()?.let {
                themeColor.setBackgroundColor(it)
            }
        }

        groupMemberSet.text = resources.getString(R.string.group_member)

        groupSetting()

        colorPicker.setOnClickListener {
            var builder = ColorPickerDialog.newBuilder()
            builder.setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setShowAlphaSlider(true)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .show(this)
        }

        saveBtn.setOnClickListener {
            count = 0
            groupSetting()
            presenter.updateGroup(groupId,groupNameEdit.text.toString(),color.toLong())
        }
        groupExitBtn.setOnClickListener {
            presenter.deleteGroup(groupId)
            finish()
        }
    }

    // 뒤로가기 버튼 누르면 이전 액티비티로 돌아가는 것을 판단해주는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if(count%2 == 1){
                    groupSetting()
                }
                else{
                    finish()
                }
            }
            R.id.update_group -> {
                count++
                if(count%2 == 0){
                    count = 0
                    groupSetting()
                    presenter.updateGroup(groupId,groupNameEdit.text.toString(),color.toLong())

                }else{
                    groupModify()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_options_in_modifygroup, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun groupSetting(){
        toolbar.title = resources.getString(R.string.setting_group)
        groupNameEdit.visibility = View.GONE
        groupNameText.visibility = View.VISIBLE
        colorPicker.visibility = View.GONE
        kakaoImg.visibility = View.VISIBLE
        kakaoText.visibility = View.VISIBLE
        saveBtn.visibility = View.INVISIBLE
        groupExitBtn.visibility = View.VISIBLE
        groupMemberSet.visibility = View.VISIBLE
    }

    private  fun groupModify(){
        toolbar.title = resources.getString(R.string.update_group)
        groupNameEdit.visibility = View.VISIBLE
        groupNameText.visibility = View.GONE
        colorPicker.visibility = View.VISIBLE
        kakaoImg.visibility = View.INVISIBLE
        kakaoText.visibility = View.INVISIBLE
        saveBtn.visibility = View.VISIBLE
        groupExitBtn.visibility = View.GONE
        groupMemberSet.visibility = View.INVISIBLE
    }

    override fun onDialogDismissed(dialogId: Int) {  }
    override fun onColorSelected(dialogId: Int, color: Int) {
        this.color = color
        themeColor.setBackgroundColor(color)
    }


}