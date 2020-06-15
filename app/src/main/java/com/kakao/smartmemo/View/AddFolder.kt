package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.kakao.message.template.*
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.kakao.smartmemo.Contract.AddGroupContract
import com.kakao.smartmemo.Presenter.AddGroupPresenter
import com.kakao.smartmemo.R
import com.kakao.util.helper.log.Logger


class AddFolder : AppCompatActivity(), ColorPickerDialogListener, AddGroupContract.View{

    lateinit var presenter : AddGroupContract.Presenter
    lateinit var toolbar: Toolbar
    lateinit var groupName : EditText
    lateinit var themeColor : View
    lateinit var colorPicker: ImageView
    lateinit var groupExitBtn : Button
    var groupId = (System.currentTimeMillis()*10000).toInt().toString()
    var color = (System.currentTimeMillis()*1000).toInt()
    var value = "1"
    private var mode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_add_group)
        presenter = AddGroupPresenter(this)

        if(intent.action == Intent.ACTION_VIEW) {
            val receivedValue = intent.data!!.getQueryParameter("value")
            val groupName = intent.data!!.getQueryParameter("group_name")
            val groupId = intent.data!!.getQueryParameter("groupId")
            Log.i("jieun", groupName)
            val intent = Intent(this, LoginActivity::class.java)
            Log.i("jieun", "AddFolder value=$receivedValue")
            intent.putExtra("value", receivedValue)
            intent.putExtra("group_name", groupName)
            intent.putExtra("groupId", groupId)
            startActivity(intent)
        }

        toolbar= findViewById(R.id.addGroupToolbar)
        toolbar.title = resources.getString(R.string.add_group)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        groupName = findViewById(R.id.editGroupName)
        themeColor = findViewById(R.id.selected_color)
        colorPicker = findViewById(R.id.color_picker)
        groupExitBtn = findViewById(R.id.group_member_exit)
        groupExitBtn.visibility = View.GONE

        themeColor.setBackgroundColor(color)

        colorPicker.setOnClickListener {
            var builder = ColorPickerDialog.newBuilder()
            builder.setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setShowAlphaSlider(true)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .show(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)

    }
    // 뒤로가기 버튼 누르면 이전 액티비티로 돌아가는 것을 판단해주는 함수
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_save -> {
                if(groupName.text.toString() != ""){
                    Log.e("jieun", "이제 다이얼로그 띄울거임!")
                    val dialog = InviteDialog(this)
                    dialog.setOnOKClickedListener { content->
                        mode = content
                        if (mode == "ok"){
                            kakaoLink()
                            presenter.addGroup(groupId,groupName.text.toString(), color)
                        }
                        else {
                            Log.e("jieun", "공유 안누름.")
                            presenter.addGroup(groupId,groupName.text.toString(), color)
                        }
                    }
                    dialog.startDialog("https://smartmemo.page.link/invite")
                }else{
                    Toast.makeText(this,"그룹이름을 작성하세요",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDialogDismissed(dialogId: Int) {  }

    override fun onColorSelected(dialogId: Int, color: Int) {
        this.color = color
        themeColor.setBackgroundColor(color)
    }

    fun kakaoLink() {
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
                        .setAndroidExecutionParams("value=$value&group_name=${groupName.text}&groupId=$groupId")
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