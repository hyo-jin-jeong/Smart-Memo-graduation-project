package com.kakao.smartmemo.Model

import android.app.Activity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.kakao.smartmemo.Contract.LoginContract
import com.kakao.smartmemo.Contract.SignUpContract
import com.kakao.smartmemo.Object.UserObject

class UserModel {
    private lateinit var onLoginListener:LoginContract.OnLoginListener
    private lateinit var onSignUpListener: SignUpContract.onSignUpListener
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    constructor() {  }

    constructor(onLoginListener: LoginContract.OnLoginListener) {
        this.onLoginListener = onLoginListener
    }

    constructor(onSignUpListener: SignUpContract.onSignUpListener) {
        this.onSignUpListener = onSignUpListener
    }

    fun getProfile(email: String) { // user 정보 받아오는 함수 : LoginSuccess일 때 사용
        firestore.collection("User").document(email).addSnapshotListener { documentSnapshot, _ ->
            if (documentSnapshot != null) {
                if(documentSnapshot.exists()){
                    with(UserObject){
                        this.email = email
                        this.addr = documentSnapshot["addr"].toString()
                        this.img_id = documentSnapshot["img_id"].toString()
                        this.img_url = documentSnapshot["img_url"].toString()
                        this.kakao_alarm_time = documentSnapshot["kakao_alarm_time"].toString()
                        this.kakao_connected = documentSnapshot["kakao_connected"] as Boolean
                        this.password = documentSnapshot["password"].toString()
                        this.user_name = documentSnapshot["user_name"].toString()
                    }
                }
            }
        }
    }

    fun addAuthUser(context: Activity,email: String,pw: String,name: String, address: String){ // user 추가하는 함수
        auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                UserObject.email = email
                UserObject.password = pw
                UserObject.user_name = name
                UserObject.addr = address
                onSignUpListener.onSuccess(task.result.toString())
            } else {
                onSignUpListener.onFailure(task.exception.toString())
            }
        }
    }

    fun addFirestoreUser() {
        firestore.collection("User").document(UserObject.email).set(UserObject)
    }

    fun addFirestoreUser(pw:String, name:String, addr:String, kakaoAlarmTime:String) {
        UserObject.password = pw
        UserObject.user_name = name
        UserObject.addr = addr
        UserObject.kakao_alarm_time = kakaoAlarmTime
        firestore.collection("User").document(UserObject.email).set(UserObject)
    }

    fun deleteUser() { // user 삭제하는 함수
        firestore.collection("User").document(UserObject.email).delete()
    }

    fun deleteAuth() {
        val user = auth.currentUser
        user?.delete()?.continueWith { task-> {
            if (task.isCanceled) {
                Log.e("auth delete Result", task.exception.toString())
            }
            Log.d("auth deleted Result", task.result.toString())
        }
        }
    }

    fun checkUser(context: Activity, email:String, password:String) { // 유효한 사용자인지 FirebaseAuth를 사용하여 확인
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context){task ->
            if(task.isSuccessful){
                auth.currentUser
                onLoginListener.onSuccess(task.result.toString())
            } else {
                onLoginListener.onFailure(task.exception.toString())
            }
        }
    }

    fun signOutUser() {
        if (auth.currentUser != null) {
            with(UserObject) {
                email=""
                password=""
                addr=""
                user_name=""
                img_id=""
                img_url=""
                kakao_connected=false
                kakao_alarm_time=""
            }
            auth.signOut()
        } else {
            Log.e("memberData에서 부른 UserModel의 signOut함수", "로그아웃 실패")
        }
    }

    fun checkPassword(confirmPassword: String): Boolean {
        return confirmPassword == UserObject.password
    }

    fun updateUserPassword(pw:String) {
        auth.currentUser!!.updatePassword(pw).addOnCompleteListener {
            if (it.isSuccessful) {
                //Toast.makeText(context, "비밀번호 변경 성공", Toast.LENGTH_SHORT).show()
                Log.e("auth password changed", "successful")
            } else
                Log.e("auth password changed", "failed")
        }
    }
}