package com.kakao.smartmemo

import android.annotation.SuppressLint

import android.content.Context

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.*

import com.kakao.smartmemo.DTO.TodoDTO
import kotlinx.android.synthetic.main.todolist_fragment.view.*

class TodoAdapter(val context: Context, private val TodoList: ArrayList<TodoDTO>) : BaseAdapter() {

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.todo_list_item, null)
        val txt_todo = view.findViewById(R.id.textView_todo) as TextView
        val bell_todo = view.findViewById(R.id.btn_todo) as Button
        val delete_todo = view.findViewById(R.id.btn_todo_delete) as Button
        val view_group = view.findViewById(R.id.group_color) as View

        view.setOnClickListener(View.OnClickListener {
            txt_todo.paintFlags = txt_todo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG  //취소선 설정
        })

        view.setOnLongClickListener(View.OnLongClickListener {

            /*val delete = bell_todo.setBackgroundResource(R.drawable.delete_todo) as Button
            delete.setOnClickListener(View.OnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("TODO LIST" + TodoList[position] + "을(를) 삭제하시겠습니까?")
                builder.setPositiveButton("확인", DialogInterface.OnClickListener {

                })
                TodoList.removeAt(position)
            })*/


            return@OnLongClickListener false
        })

        bell_todo.setOnClickListener(View.OnClickListener {
            bell_todo.setBackgroundResource(R.drawable.bell_icon_on)
            val alarmSettingsIntent = Intent(it.context, TodoListActivity::class.java)
            Log.v("seyuuuun", "Intent")
            it.context.startActivity(alarmSettingsIntent)
            Log.v("seyuuuun", "Intent2")
        })

        val todo = TodoList[position]
        txt_todo.text = todo.todoContent

        view_group.setBackgroundColor(Color.parseColor("#B2CCFF"))
        return view
    }

    override fun getItem(position: Int) : TodoDTO {
        return TodoList[position]
    }

    override fun getItemId(position: Int): Long {
       return 0
    }

    override fun getCount(): Int {
       return TodoList.size
    }

}