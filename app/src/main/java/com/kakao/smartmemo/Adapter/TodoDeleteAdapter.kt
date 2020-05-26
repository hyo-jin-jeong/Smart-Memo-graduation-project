package com.kakao.smartmemo.Adapter

import android.annotation.SuppressLint

import android.content.Context

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kakao.smartmemo.Contract.TodoDeleteAdapterContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.todo_list_delete.view.*

class TodoDeleteAdapter(val context: Context, private val todoList: ArrayList<TodoData>) : BaseAdapter(), TodoDeleteAdapterContract.Model, TodoDeleteAdapterContract.View {

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list_delete, null)
        val todo = todoList[position]
        var pos = arrayListOf<Int>()
        val checkbox_todo = view.findViewById(R.id.checkDelete) as CheckBox
        view.textView_todo.text = todo.todoContent

        var checkedTodo = false

        view.group_color.setBackgroundColor(Color.parseColor("#B2CCFF"))


        if(checkbox_todo.isChecked) {
            pos.add(position)
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

}