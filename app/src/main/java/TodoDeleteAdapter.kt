package com.kakao.smartmemo

import android.annotation.SuppressLint

import android.content.Context

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.*
import com.kakao.smartmemo.DTO.TodoDTO

class TodoDeleteAdapter(val context: Context, private val TodoList: ArrayList<TodoDTO>) : BaseAdapter() {

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.todo_list_delete, null)
        val txt_todo = view.findViewById(R.id.textView_todo) as TextView
        val view_group = view.findViewById(R.id.group_color) as View


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