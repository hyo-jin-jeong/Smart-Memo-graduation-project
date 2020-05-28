package com.kakao.smartmemo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ListView
import com.kakao.smartmemo.Contract.TodoDeleteAdapterContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.todo_list_delete.view.*


class TodoDeleteAdapter(val context: Context, private val todoList: ArrayList<TodoData>) : BaseAdapter(), TodoDeleteAdapterContract.Model, TodoDeleteAdapterContract.View {

    var pos = arrayListOf<Int>()

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list_delete, null)
        val todo = todoList[position]

        val checkbox_todo = view.findViewById(R.id.checkDelete) as CheckBox
        view.textView_todo.text = todo.todoContent

        //checkbox_todo.isClickable = false  //체크박스 선택못하도록

        view.group_color.setBackgroundColor(Color.parseColor("#B2CCFF"))
        checkbox_todo.setChecked((parent as ListView).isItemChecked(position))

        view.textView_todo.setOnClickListener { //리스트뷰 눌렀을시 체크박스 선택
            checkbox_todo.isChecked = true
            pos.add(position)
            Log.v("seyuuuun", position.toString())
        }

        return view
    }

    override fun getItem(position: Int) : TodoData {
        return todoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position as Long
    }

    override fun getCount(): Int {
        return todoList.size
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getTodoContent() {

    }

    override fun deleteTodo(position: Int) {
        todoList.removeAt(position)
    }

    override fun getGroup() {

    }

    fun selectedTodo() : ArrayList<Int>{
        return pos
    }
}