package com.kakao.smartmemo.Model

import android.app.Activity
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.smartmemo.Contract.LoginContract
import com.kakao.smartmemo.Contract.SignUpContract
import com.kakao.smartmemo.Object.UserObejct

class UserModel {
    private lateinit var onLoginListener:LoginContract.onLoginListener
    private lateinit var onSignUpListener: SignUpContract.onSignUpListener
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    constructor() {  }

    constructor(onLoginListener: LoginContract.onLoginListener) {
        this.onLoginListener = onLoginListener
    }

    constructor(onSignUpListener: SignUpContract.onSignUpListener) {
        this.onSignUpListener = onSignUpListener
    }

    fun getProfile(email: String) { // user 정보 받아오는 함수
        firestore.collection("User").document(email).get()
    }

    fun addAuthUser(context: Activity,email: String,pw: String,name: String, address: String){ // user 추가하는 함수
        auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                UserObejct.email = email
                UserObejct.password = pw
                UserObejct.user_name = name
                UserObejct.addr = address
                onSignUpListener.onSuccess(task.result.toString())
            } else {
                onSignUpListener.onFailure(task.exception.toString())
            }
        }

    }

    fun addFirestoreUser() {
        firestore.collection("User").document(UserObejct.email).set(UserObejct)
    }

    fun updateUser() { // user 정보 수정하는 함수

    }

    fun deleteUser() { // user 삭제하는 함수

    }
    fun checkUser(context: Activity, email:String, password:String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context){task ->
                if(task.isSuccessful){
                    onLoginListener.onSuccess(task.result.toString())
                } else {
                    onLoginListener.onFailure(task.exception.toString())
                }
        }
    }

    fun checkPassword(confirmPassword: EditText?): Boolean { //
            return true
    }
}