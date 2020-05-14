package com.kakao.smartmemo.View

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
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
    var color : Int = -13184
    lateinit var groupName : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_add_group)
        presenter = AddGroupPresenter(this)

        groupName = findViewById(R.id.editGroupName)
        val themeColor : View = findViewById(R.id.selected_color)
        //val kakao_member
        themeColor.setBackgroundColor(color)
        val toolBar:Toolbar = findViewById(R.id.addGroupToolbar)
        toolBar.title = resources.getString(R.string.add_group)
        setSupportActionBar(toolBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var colorPicker = findViewById<ImageView>(R.id.color_picker)
        colorPicker.setOnClickListener {
            var builder = ColorPickerDialog.newBuilder()
            builder.setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setShowAlphaSlider(true)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .show(this)
        }

        var saveBtn = save_group
        saveBtn.setOnClickListener {
            presenter.addGroup(groupName.text.toString(),color)
            finish()
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

    override fun onDialogDismissed(dialogId: Int) {  }

    override fun onColorSelected(dialogId: Int, color: Int) {
        val selectedColor = selected_color
        this.color = color
        selectedColor.setBackgroundColor(color)
    }

}