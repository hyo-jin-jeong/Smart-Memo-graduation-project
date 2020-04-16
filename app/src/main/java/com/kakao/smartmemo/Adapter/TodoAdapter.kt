package com.kakao.smartmemo.com.kakao.smartmemo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.R
import com.kakao.smartmemo.TodoListActivity
import kotlinx.android.synthetic.main.todo_list_item.view.*


class TodoAdapter(val context: Context, private val todoList: ArrayList<TodoData>) : BaseAdapter() {

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list_item, null)
        val todo = todoList[position]

        var checkedTodo = false
        view.textView_todo.text = todo.todoContent
        view.group_color.setBackgroundColor(Color.parseColor("#B2CCFF"))
        view.textView_todo.setOnClickListener() { // 취소선 ( 성 공 )
            if (checkedTodo) { // todolist에 취소선이 그어져 있으면 true
                view.textView_todo.paintFlags = 0
                checkedTodo = false
            } else { // todolist에 취소선이 그어져 있지 않으면 false
                view.textView_todo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //취소선 설정
                checkedTodo = true
            }


        }

        view.btn_todo.setOnClickListener() { // 종모양 누르면 알람 설정 페이지로 이동 ( 성공 )
            view.btn_todo.setBackgroundResource(R.drawable.bell_icon_on)

            val alarmSettingsIntent = Intent(it.context, TodoListActivity::class.java)
            it.context.startActivity(alarmSettingsIntent)
        }
        return view
    }

    override fun getItem(position: Int) : TodoData {
        return todoList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return todoList.size
    }

}