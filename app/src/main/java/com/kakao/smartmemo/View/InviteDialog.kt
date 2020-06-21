package com.kakao.smartmemo.View

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.kakao.smartmemo.R

class InviteDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var listener : MyDialogOKClickedListener

    fun startDialog(link: String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.invite_dialog)
        dialog.setCancelable(false)

        val linkText = dialog.findViewById<TextView>(R.id.link)
        linkText.text = link

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

}