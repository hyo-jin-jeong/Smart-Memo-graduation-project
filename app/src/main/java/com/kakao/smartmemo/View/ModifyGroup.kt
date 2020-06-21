package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.kakao.smartmemo.Contract.ModifyGroupContract
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Presenter.ModifyGroupPresenter
import com.kakao.smartmemo.R
import com.kakao.util.helper.log.Logger

class ModifyGroup : AppCompatActivity(), ColorPickerDialogListener, ModifyGroupContract.View{

    lateinit var presenter: ModifyGroupContract.Presenter
    lateinit var toolbar: Toolbar
    private lateinit var folderNameEdit : EditText
    private lateinit var folderNameText :  TextView
    private lateinit var themeColor : View
    private lateinit var folderMemberSet:TextView
    private lateinit var colorPicker: ImageView
    private lateinit var groupExitBtn : Button
    private lateinit var kakaoImg : ImageView
    private lateinit var kakaoText : TextView
    lateinit var folderId : String

    private var count = 0
    private var color = 0

    private var value = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_modify_group)

        if(intent.action == Intent.ACTION_VIEW) {
            val receivedValue = intent.data!!.getQueryParameter("value")
            val groupName = intent.data!!.getQueryParameter("group_name")
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("value", receivedValue)
            intent.putExtra("group_name", groupName)
            startActivity(intent)
        }

        presenter = ModifyGroupPresenter(this)

        toolbar= findViewById(R.id.addGroupToolbar)
        toolbar.title = resources.getString(R.string.setting_group)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        folderNameEdit = findViewById(R.id.editGroupName)
        folderNameText = findViewById(R.id.textGroupName)
        themeColor = findViewById(R.id.selected_color)
        colorPicker = findViewById(R.id.color_picker)
        folderMemberSet = findViewById(R.id.group_invitation)
        groupExitBtn = findViewById(R.id.group_member_exit)
        kakaoImg = findViewById(R.id.kakao_icon)
        kakaoText = findViewById(R.id.kakao_text)

        val kakaoLink = findViewById<LinearLayout>(R.id.kakao_link)
        kakaoLink.setOnClickListener {
            kakaoLink()
        }

        if (intent.hasExtra("folderId")) {
            folderId = intent.getStringExtra("folderId")
            folderNameEdit.setText(FolderObject.folderInfo[folderId])
            folderNameText.text = FolderObject.folderInfo[folderId].toString()
            themeColor.setBackgroundColor(FolderObject.folderColor[folderId]!!.toInt())
            this.color = FolderObject.folderColor[folderId]!!.toInt()
        }

        folderMemberSet.text = resources.getString(R.string.group_member)

        groupSetting()

        colorPicker.setOnClickListener {
            var builder = ColorPickerDialog.newBuilder()
            builder.setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setShowAlphaSlider(true)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .show(this)
        }
        groupExitBtn.setOnClickListener {
            presenter.deleteGroup(folderId)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_options_in_modifygroup, menu)
        return super.onCreateOptionsMenu(menu)
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
                    groupSetting()
                    presenter.updateGroup(folderId,folderNameEdit.text.toString(),color.toLong())
                } else{
                    groupModify()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun groupSetting(){
        toolbar.title = resources.getString(R.string.setting_group)
        folderNameEdit.visibility = View.GONE
        folderNameText.visibility = View.VISIBLE
        colorPicker.visibility = View.GONE
        kakaoImg.visibility = View.VISIBLE
        kakaoText.visibility = View.VISIBLE
        groupExitBtn.visibility = View.VISIBLE
        folderMemberSet.visibility = View.VISIBLE
    }

    private  fun groupModify(){
        toolbar.title = resources.getString(R.string.update_group)
        folderNameEdit.visibility = View.VISIBLE
        folderNameText.visibility = View.GONE
        colorPicker.visibility = View.VISIBLE
        kakaoImg.visibility = View.INVISIBLE
        kakaoText.visibility = View.INVISIBLE
        groupExitBtn.visibility = View.GONE
        folderMemberSet.visibility = View.INVISIBLE
    }

    override fun onDialogDismissed(dialogId: Int) {  }
    override fun onColorSelected(dialogId: Int, color: Int) {
        this.color = color
        themeColor.setBackgroundColor(color)
    }

    private fun kakaoLink() {
        val params = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    "Smart Memo",
                    "https://github.com/hyo-jin-jeong/GraduationPortfolio/blob/master/app/src/main/res/drawable/app_logo_no_title.jpeg?raw=true",
                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com").build()
                )
                    .setDescrption("초대 링크를 누르면 공유 폴더에 참가할 수 있습니다.")
                    .build()
            )
            .addButton(
                ButtonObject(
                    "앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("https://smartmemo.page.link/invite")
                        .setAndroidExecutionParams("value=$value&group_name=${folderNameText.text}")
                        .build()
                )
            )
            .build()

        val serverCallbackArgs: MutableMap<String, String> =
            HashMap()
        serverCallbackArgs["user_id"] = "\${current_user_id}"
        serverCallbackArgs["product_id"] = "\${shared_product_id}"

        KakaoLinkService.getInstance().sendDefault(
            this,
            params,
            serverCallbackArgs,
            object : ResponseCallback<KakaoLinkResponse?>() {
                override fun onFailure(errorResult: ErrorResult) {
                    Logger.e(errorResult.toString())
                }

                override fun onSuccess(result: KakaoLinkResponse?) { // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                }
            })
    }

}