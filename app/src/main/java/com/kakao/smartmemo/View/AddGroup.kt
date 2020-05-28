package com.kakao.smartmemo.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.kakao.smartmemo.Presenter.AddGroupPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Contract.AddGroupContract
import kotlinx.android.synthetic.main.content_add_group.*

class AddGroup : AppCompatActivity(), ColorPickerDialogListener, AddGroupContract.View{

    lateinit var presenter : AddGroupContract.Presenter
    lateinit var toolbar: Toolbar
    lateinit var groupName : EditText
    lateinit var themeColor : View
    lateinit var colorPicker: ImageView
    lateinit var saveBtn:Button
    lateinit var groupExitBtn : Button
    var color : Int = -13184
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_add_group)
        presenter = AddGroupPresenter(this)

        toolbar= findViewById(R.id.addGroupToolbar)
        toolbar.title = resources.getString(R.string.add_group)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        groupName = findViewById(R.id.editGroupName)
        themeColor = findViewById(R.id.selected_color)
        colorPicker = findViewById(R.id.color_picker)
        saveBtn = findViewById(R.id.save_group)
        groupExitBtn = findViewById(R.id.group_member_exit)
        groupExitBtn.visibility = View.GONE
        //val kakao_member

        themeColor.setBackgroundColor(color)

        colorPicker.setOnClickListener {
            var builder = ColorPickerDialog.newBuilder()
            builder.setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setShowAlphaSlider(true)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .show(this)
        }


        saveBtn.setOnClickListener {
            if(groupName.text.toString() != ""){
                presenter.addGroup(groupName.text.toString(),color)
                finish()
            }else{
                Toast.makeText(this,"그룹이름을 작성하세요",Toast.LENGTH_SHORT).show()
            }

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

    override fun onDialogDismissed(dialogId: Int) {  }

    override fun onColorSelected(dialogId: Int, color: Int) {
        this.color = color
        themeColor.setBackgroundColor(color)
    }

}