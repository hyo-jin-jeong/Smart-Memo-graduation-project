package com.kakao.smartmemo.Model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.smartmemo.Object.UserObejct
import com.kakao.smartmemo.Presenter.MemberDataPresenter
import com.kakao.smartmemo.View.SignInActivity

class UserModel {
    lateinit var presenter: MemberDataPresenter
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getProfile() { // user 정보 받아오는 함수
        firestore.collection("User").document(UserObejct.email).get()
        //Log.e("firestore 정보 " , firestore.collection("User").document(UserObejct.email).get().toString())

    }

    fun addAuthUser(context: Activity, email: String, pw: String, name: String, address: String) : Boolean{ // user 추가하는 함수
        auth.createUserWithEmailAndPassword(email,pw)?.addOnCompleteListener(context) {task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
            } else {
                Log.e("USERMODE", "SUCCESS?")
            }
        }
        UserObejct.email = email
        UserObejct.password = pw
        UserObejct.user_name = name
        UserObejct.addr = address

        return true
    }

    fun addFirestoreUser() {
        firestore.collection("User").document(UserObejct.email).set(UserObejct)
    }

    fun updateUser() { // user 정보 수정하는 함수

    }

    fun deleteUser() { // user 삭제하는 함수

    }

    fun checkUser(context: Activity, email:String, password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                } else {
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun checkPassword(confirmPassword: EditText?): Boolean { //

        return true
    }
}