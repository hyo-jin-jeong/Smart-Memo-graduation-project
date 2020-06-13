package com.kakao.smartmemo.View

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kakao.smartmemo.Contract.LoginContract
import com.kakao.smartmemo.Presenter.LoginPresenter
import com.kakao.smartmemo.R

class LoginActivity: AppCompatActivity(), LoginContract.View {
    private lateinit var email: EditText
    private lateinit var pw: EditText
    private lateinit var loginDialog : ProgressDialog
    lateinit var presenter : LoginContract.Presenter
    private var loginCount = 0

    private val GPS_ENABLE_REQUEST_CODE: Int = 2001
    private val PERMISSIONS_REQUEST_CODE: Int = 100
    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        presenter = LoginPresenter(this)
        loginDialog = ProgressDialog(this)

        val loginButton = findViewById<CardView>(R.id.login_button)
        email = findViewById(R.id.email)
        pw = findViewById(R.id.pw)

        loginButton.setOnClickListener {
            loginCount++
            if (email.text.toString() != "" && pw.text.toString() != "" && loginCount == 1) {
                loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                loginDialog.setMessage("로그인 하는 중")
                loginDialog.show()
                presenter.checkUser(this, email.text.toString(), pw.text.toString())
            } else {
                Toast.makeText(applicationContext, "로그인 중입니다. 잠시만 기다려주십시오.", Toast.LENGTH_SHORT).show()
            }
            //else {
                //Toast.makeText(this, "다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                // 나중에 애니메이션 넣기
            //}
        }

        val searchInfoButton = findViewById<Button>(R.id.search_id_pw)
        searchInfoButton.setOnClickListener {
            Toast.makeText(applicationContext, "찾기 클릭", Toast.LENGTH_SHORT).show()
        }
        val signUpButton = findViewById<Button>(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        when {
            !checkLocationServicesStatus() -> {
                showDialogForLocationServiceSetting()
            }
            else -> {
                checkRunTimePermission()
            }
        }

    }
//    졸작 최종 완성되면 주석해제하기
//    override fun onResume() {
//        super.onResume()
//        if (UserObject.email != null && UserObject.password != null) {
//            email.setText(UserObject.email)
//            pw.setText(UserObject.password)
//        }
//    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onLoginSuccess() {
        loginCount = 0
        loginDialog.dismiss()
        startMainActivity()
    }
    override fun onLoginFailure(message: String) {
        loginCount = 0
        loginDialog.dismiss()
        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode === PERMISSIONS_REQUEST_CODE && grantResults.size === REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                Log.d("@@@", "start")

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS.get(0)
                    )
                ) {
                    Toast.makeText(
                        this@LoginActivity,
                        "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@LoginActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@LoginActivity,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(
                    this@LoginActivity,
                    "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Toast.LENGTH_LONG
                )
                    .show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@LoginActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.

                ActivityCompat.requestPermissions(
                    this@LoginActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder =
            AlertDialog.Builder(this@LoginActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
                앱을 사용하기 위해서는 위치 서비스가 필요합니다.
                위치 설정을 수정하실래요?
                """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
    }

    private fun checkLocationServicesStatus(): Boolean {
        val locationManager: LocationManager? = this@LoginActivity.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager?
        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }
}