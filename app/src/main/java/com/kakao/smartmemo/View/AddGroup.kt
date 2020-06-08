package com.kakao.smartmemo.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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

class AddGroup : AppCompatActivity(), ColorPickerDialogListener, AddGroupContract.View{

    lateinit var presenter : AddGroupContract.Presenter
    lateinit var toolbar: Toolbar
    lateinit var groupName : EditText
    lateinit var themeColor : View
    lateinit var colorPicker: ImageView
    lateinit var saveBtn:Button
    lateinit var groupExitBtn : Button
    var color = (System.currentTimeMillis()*1000).toInt()
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
        val kakaoLink = findViewById<LinearLayout>(R.id.kakao_link)
        kakaoLink.setOnClickListener {
            kakaoLink()
        }

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

    fun kakaoLink() {
        val params = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                        "디저트 사진",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                            .setMobileWebUrl("https://developers.kakao.com").build()
                    )
                    .setDescrption("아메리카노, 빵, 케익")
                    .build()
            )
            .setSocial(
                SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                    .setSharedCount(30).setViewCount(40).build()
            )
            .addButton(
                ButtonObject(
                    "웹에서 보기",
                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl(
                        "https://developers.kakao.com"
                    ).build()
                )
            )
            .addButton(
                ButtonObject(
                    "앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("https://smartmemo.page.link/invite")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()
                )
            )
            .build()

        val serverCallbackArgs: MutableMap<String, String> =
            HashMap()
        serverCallbackArgs["user_id"] = "\${current_user_id}"
        serverCallbackArgs["product_id"] = "\${shared_product_id}"

//        KakaoLinkService.getInstance().sendDefault(
//            this,
//            params,
//            serverCallbackArgs,
//            object : ResponseCallback<KakaoLinkResponse?>() {
//                override fun onFailure(errorResult: ErrorResult) {
//                    Logger.e(errorResult.toString())
//                }
//
//                override fun onSuccess(result: KakaoLinkResponse?) { // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
//                }
//            })

        val url = "https://smartmemo.page.link/invite"
        KakaoLinkService.getInstance()
            .sendScrap(this, url, null, object : ResponseCallback<KakaoLinkResponse>() {
                override fun onFailure(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "카카오링크 공유 실패: $errorResult")
                }

                override fun onSuccess(result: KakaoLinkResponse) {
                    Log.i("KAKAO_API", "카카오링크 공유 성공")

                    // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w("KAKAO_API", "warning messages: " + result.warningMsg)
                    Log.w("KAKAO_API", "argument messages: " + result.argumentMsg)
                }
            })
    }

}