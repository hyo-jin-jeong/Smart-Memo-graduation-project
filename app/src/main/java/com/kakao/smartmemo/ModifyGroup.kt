package com.kakao.smartmemo

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import kotlinx.android.synthetic.main.content_add_group.*


class ModifyGroup : AppCompatActivity(), ColorPickerDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_add_group)

        val toolBar:Toolbar = findViewById(R.id.addGroupToolbar)
        toolBar.title = resources.getString(R.string.setting_group)
        setSupportActionBar(toolBar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("groupName")) {
            editGroupName.setText(intent.getStringExtra("groupName"))
        }

        group_invitation.text = resources.getString(R.string.group_member)

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
        selectedColor.setBackgroundColor(color)
    }

}