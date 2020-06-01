package com.kakao.smartmemo.Model

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kakao.smartmemo.Contract.LoginContract
import com.kakao.smartmemo.Contract.MemberChangeContract
import com.kakao.smartmemo.Contract.MemberDataContract
import com.kakao.smartmemo.Contract.SignUpContract
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.Object.UserObject


class UserModel {
    private lateinit var onLoginListener: LoginContract.OnLoginListener
    private lateinit var onSignUpListener: SignUpContract.onSignUpListener
    private lateinit var onPasswordChangeListener: MemberChangeContract.OnPasswordChangeSuccessListener
    private lateinit var onDeleteUserListener: MemberDataContract.OnDeleteUserListener
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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
    fun getProfile() { // user 정보 받아오는 함수
        val fireStoreUser = firestore.collection("User").document("${UserObject.email}")
        fireStoreUser.addSnapshotListener { documentSnapshot, _ ->
            if (documentSnapshot != null) {
                if (documentSnapshot.exists()) {
                    with(UserObject) {
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
    fun addAuthUser(
        context: Activity,
        email: String,
        pw: String,
        name: String,
        address: String
    ) { // user 추가하는 함수
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
        val groupId = UserObject.email + System.currentTimeMillis()
        firestore.collection("User").document("${UserObject.email}").set(UserObject)
        firestore.collection("User").document("${UserObject.email}")
            .collection("GroupInfo").document("GroupId").set(hashMapOf(groupId to "내 폴더"))
        firestore.collection("Group").document(groupId)
            .set(hashMapOf("group_name" to "내 폴더", "group_color" to -1234))
        firestore.collection("Group").document(groupId).collection("MemberInfo")
            .document("MemberEmail").set(
                hashMapOf(UserObject.email to UserObject.email), SetOptions.merge()
            )
    }
    fun updateFirestoreUser(pw: String, name: String, addr: String, kakaoAlarmTime: String) {
        UserObject.password = pw
        UserObject.user_name = name
        UserObject.addr = addr
        UserObject.kakao_alarm_time = kakaoAlarmTime
        firestore.collection("User").document(UserObject.email).set(UserObject)
    }
    fun deleteUser() { // collection에서 user 삭제하는 함수
        firestore.collection("User").document(UserObject.email).collection("GroupInfo")
            .document("GroupId").delete()
        firestore.collection("User").document(UserObject.email).delete()
        GroupObject.groupInfo.forEach { group ->
            firestore.collection("Group").document(group.key).collection("MemberInfo")
                .document("MemberEmail").addSnapshotListener { memberSnapshot, _ ->
                    if (memberSnapshot?.data?.count() == 1) {
                        firestore.collection("Group").document(group.key).collection("MemoInfo")
                            .document("MemoId").addSnapshotListener { memoSnapshot, _ ->
                                firestore.collection("Group").document(group.key)
                                    .collection("TodoInfo")
                                    .document("todoId").addSnapshotListener { todoSnapshot, _ ->

                                        memoSnapshot?.data?.forEach { memoDeleteSnapshot ->
                                            firestore.collection("Memo")
                                                .document(memoDeleteSnapshot.value as String)
                                                .collection("Place")
                                                .document("PlaceInfo").delete()
                                            firestore.collection("Memo")
                                                .document(memoDeleteSnapshot.value as String)
                                                .delete()
                                        }
                                        todoSnapshot?.data?.forEach { todoDeleteSnapshot ->
                                            firestore.collection("Todo")
                                                .document(todoDeleteSnapshot.value as String)
                                                .collection("TimeAlarm")
                                                .addSnapshotListener { timeSnapshot, _ ->
                                                    firestore.collection("Todo")
                                                        .document(todoDeleteSnapshot.value as String)
                                                        .collection("PlaceAlarm")
                                                        .addSnapshotListener { placeSnapshot, _ ->
                                                            placeSnapshot?.forEach {
                                                                it.reference.delete()
                                                            }

                                                            timeSnapshot?.forEach {
                                                                it.reference.delete()

                                                            }
                                                            firestore.collection("Todo")
                                                                .document(todoDeleteSnapshot.value as String)
                                                                .delete()
                                                        }
                                                }
                                        }
                                        firestore.collection("Group").document(group.key)
                                            .collection("TodoInfo").document("todoId").delete()
                                        firestore.collection("Group").document(group.key)
                                            .collection("MemoInfo").document("MemoId").delete()
                                        firestore.collection("Group").document(group.key)
                                            .collection("MemberInfo").document("MemberEmail")
                                            .delete()
                                        firestore.collection("Group").document(group.key).delete()
                                    }
                            }

                    } else {
                        memberSnapshot?.reference?.delete()
                    }
                }
        }
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
                GroupObject.groupInfo.clear()
                GroupObject.groupColor.clear()
                auth.currentUser
                UserObject.email = email
                firestore.collection("User").document("${UserObject.email}").collection("GroupInfo")
                    .document("GroupId")
                    .addSnapshotListener { groupSnapshot, firebaseFirestoreException ->
                        groupSnapshot?.data?.forEach {
                            GroupObject.groupInfo[it.key] = it.value as String
                        }
                    }
                onLoginListener.onSuccess(task.result.toString())
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
            with(GroupObject) {
                this.groupInfo.clear()
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
}