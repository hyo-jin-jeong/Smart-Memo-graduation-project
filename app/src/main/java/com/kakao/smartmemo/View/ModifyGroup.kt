package com.kakao.smartmemo.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Contract.ModifyGroupContract
import com.kakao.smartmemo.Presenter.ModifyGroupPresenter
import kotlinx.android.synthetic.main.content_add_group.*


class ModifyGroup : AppCompatActivity(), ModifyGroupContract.View{

    lateinit var presenter: ModifyGroupContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_add_group)
        presenter = ModifyGroupPresenter(this)

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
            R.id.update_group -> {
                presenter.updateGroup()

            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_options_in_modifygroup, menu)
        return super.onCreateOptionsMenu(menu)
    }

}