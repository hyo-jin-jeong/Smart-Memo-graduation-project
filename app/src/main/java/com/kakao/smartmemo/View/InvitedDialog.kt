package com.kakao.smartmemo.View

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.kakao.smartmemo.R

class InvitedDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var listener : MyDialogOKClickedListener

    fun startDialog(groupName: String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.invited_dialog)
        dialog.setCancelable(false)

        val title = dialog.findViewById<TextView>(R.id.kakao_send_text)
        title.text = "$groupName 폴더"

        val okButton = dialog.findViewById<Button>(R.id.ok)
        okButton.setOnClickListener {
            listener.onOKClicked("ok")
            dialog.dismiss()
        }

        val cancelButton = dialog.findViewById<Button>(R.id.cancel)
        cancelButton.setOnClickListener {
            listener.onOKClicked("cancel")
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }


    interface MyDialogOKClickedListener {
        fun onOKClicked(content : String)
    }
    fun dialogDismiss(){
        dialog.dismiss()
        Log.e("dd","finish")
    }

}