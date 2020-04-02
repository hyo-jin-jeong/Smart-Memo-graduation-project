package com.kakao.smartmemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    private lateinit var id: String
    private lateinit var pw: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)



        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            //id = findViewById<EditText>(R.id.id).toString()
            //pw = findViewById<EditText>(R.id.pw).toString()
            //if(id == "admin" && pw == "1111") {
                startActivity(Intent(this,MainActivity::class.java))
            //}
        }

        val searchInfoButton = findViewById<Button>(R.id.search_id_pw)
        searchInfoButton.setOnClickListener {
            Toast.makeText(applicationContext, "찾기 클릭", Toast.LENGTH_SHORT)
        }
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }
}