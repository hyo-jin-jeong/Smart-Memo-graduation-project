package com.kakao.smartmemo.Model

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.smartmemo.Contract.*
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Object.UserObject


class UserModel {
    private lateinit var onLoginListener: LoginContract.OnLoginListener
    private lateinit var onSignUpListener: SignUpContract.onSignUpListener
    private lateinit var onPasswordChangeListener: MemberChangeContract.OnPasswordChangeSuccessListener
    private lateinit var onDeleteUserListener: MemberDataContract.OnDeleteUserListener
    private lateinit var onMainListener: MainContract.onMainListener
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser = FirebaseDatabase.getInstance().reference.child("User")
    private var firebaseGroup = FirebaseDatabase.getInstance().reference.child("Group")

    constructor()

    constructor(onLoginListener: LoginContract.OnLoginListener) {
        this.onLoginListener = onLoginListener
    }

    constructor(onSignUpListener: SignUpContract.onSignUpListener) {
        this.onSignUpListener = onSignUpListener
    }

    constructor(onPasswordChangeListener: MemberChangeContract.OnPasswordChangeSuccessListener) {
        this.onPasswordChangeListener = onPasswordChangeListener
    }

    constructor(onDeleteUserListener: MemberDataContract.OnDeleteUserListener) {
        this.onDeleteUserListener = onDeleteUserListener
    }
    constructor(onMainListener:MainContract.onMainListener) {
        this.onMainListener = onMainListener
    }

    fun getProfile() { // user 정보 받아오는 함수
        firebaseUser.child(UserObject.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapShot: DataSnapshot) {
                UserObject.uid = snapShot.child("uid").value.toString()
                UserObject.email = snapShot.child("email").value.toString()
                UserObject.password = snapShot.child("password").value.toString()
                UserObject.user_name = snapShot.child("user_name").value.toString()
                UserObject.addr = snapShot.child("addr").value.toString()
                UserObject.kakao_connected =
                    snapShot.child("kakao_connected").value.toString().toBoolean()
                UserObject.kakao_alarm_time = snapShot.child("kakao_alarm_time").value.toString()
                onLoginListener.onSuccess()
            }
        })


    }

    fun addAuthUser(
        context: Activity,
        email: String,
        pw: String,
        name: String,
        address: String
    ) { // user 추가하는 함수
        auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                UserObject.uid = auth.uid.toString()
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
        val groupId = "기본폴더" + System.currentTimeMillis()
        firebaseUser.updateChildren(
            mapOf(UserObject.uid to UserObject)
        )
        firebaseUser.child(UserObject.uid).child("GroupInfo").setValue(mapOf(groupId to "기본폴더"))

        with(firebaseGroup.child(groupId)){
            setValue(mapOf("folderColor" to 1549068184))
            updateChildren(mapOf("folderName" to "기본폴더"))
            child("MemberInfo").updateChildren(mapOf(UserObject.uid to UserObject.email))
        }
    }

    fun updateDatabaseUser(pw: String, name: String, addr: String, kakaoAlarmTime: String) {
        UserObject.password = pw
        UserObject.user_name = name
        UserObject.addr = addr
        UserObject.kakao_alarm_time = kakaoAlarmTime
        with(firebaseUser.child(UserObject.uid)) {
            this.child("password").setValue(pw)
            this.child("user_name").setValue(name)
            this.child("addr").setValue(addr)
            this.child("kakao_")
        }
    }

    fun deleteUser() { // collection에서 user 삭제하는 함수

    }

    fun deleteAuth() { // authentication에서 사용자 삭제하는 함수
        val user = auth.currentUser
        user?.delete()?.continueWith { task ->
            if (task.isSuccessful) {
                auth.signOut()
            } else {
                Log.e("auth delete Result", task.exception.toString())
                //onDeleteUserListener.onSuccess()
            }

        }
    }

    fun checkUser(
        context: Activity,
        email: String,
        password: String
    ) { // 유효한 사용자인지 FirebaseAuth를 사용하여 확인
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                with(UserObject) {
                    UserObject.email = ""
                    UserObject.password = ""
                    addr = ""
                    user_name = ""
                    img_id = ""
                    img_url = ""
                    kakao_connected = false
                    kakao_alarm_time = ""
                }
                FolderObject.folderInfo.clear()
                FolderObject.folderColor.clear()
                auth.currentUser
                UserObject.uid = auth.uid.toString()
                UserObject.email = email
                getProfile()
            } else {
                onLoginListener.onFailure(task.exception.toString())
            }
        }
    }

    fun signOutUser() {
        if (auth.currentUser != null) {
            with(UserObject) {
                email = ""
                password = ""
                addr = ""
                user_name = ""
                img_id = ""
                img_url = ""
                kakao_connected = false
                kakao_alarm_time = ""
            }
            with(FolderObject) {
                this.folderInfo.clear()
            }
            auth.signOut()
        } else {
            Log.e("Logout", "logout failed")
        }
    }

    fun checkPassword(confirmPassword: String): Boolean {
        return confirmPassword == UserObject.password
    }

    fun updateUserPassword(pw: String) {
        var user = auth.currentUser

        user?.updatePassword(pw)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("auth password changed", "successful")
                onPasswordChangeListener.onSuccess()
            } else {
                Log.e("auth password changed", "failed")
                onPasswordChangeListener.onFailure()
            }
        }
    }

    fun checkFolderMember(groupId: String?, groupName: String?) {
        firebaseGroup.child(groupId.toString()).child("MemberInfo").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(memberSnapshot: DataSnapshot) {
                memberSnapshot.children.forEach {userEmail->
                    if(userEmail.value != UserObject.email){
                        memberSnapshot.ref.updateChildren(mapOf(UserObject.uid to UserObject.email))
                        firebaseUser.child(UserObject.uid).child("GroupInfo").updateChildren(mapOf(groupId to groupName))
                        onMainListener.onSuccess()
                    }

                }
            }

        })
    }
}