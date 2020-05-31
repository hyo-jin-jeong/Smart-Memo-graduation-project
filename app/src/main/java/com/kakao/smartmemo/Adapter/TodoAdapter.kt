package com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kakao.smartmemo.Contract.TodoAdapterContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoAdapter(val context: Context, private val todoList: MutableList<TodoData>) : BaseAdapter(), TodoAdapterContract.Model, TodoAdapterContract.View {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list_item, null)
        val todo = todoList[position]
        var checkedTodo = false
        view.textView_todo.text = todo.title
        view.group_color.setBackgroundColor(todo.groupColor.toInt())
        view.group_name.text = todo.groupName
        if(!todo.setPlaceAlarm && !todo.setTimeAlarm){
            view.img_todo.visibility = View.INVISIBLE
        }
        return view
    }

    override fun getItem(position: Int) : TodoData {
        return todoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return todoList.size
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getTodoContent() {

    }

    override fun getGroup() {

    }

}