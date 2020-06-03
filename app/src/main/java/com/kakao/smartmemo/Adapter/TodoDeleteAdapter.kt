package com.kakao.smartmemo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kakao.smartmemo.Contract.TodoDeleteAdapterContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.todo_list_delete.view.*

class TodoDeleteAdapter(val context: Context, private val todoList: MutableList<TodoData>) : BaseAdapter(), TodoDeleteAdapterContract.Model, TodoDeleteAdapterContract.View {

    var pos = arrayListOf<Int>()
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list_delete, null)
        val todo = todoList[position]
        var click = 0

        view.delete_group_name.text = todo.groupName
        val checkbox_todo = view.findViewById(R.id.checkDelete) as CheckBox
        val textView_todo = view.findViewById(R.id.textView_todo) as TextView
        val layout = view.findViewById(R.id.layout_Todo) as LinearLayout
        view.textView_todo.text = todo.title

        //checkbox_todo.isClickable = false  //체크박스 선택못하도록

        view.group_color.setBackgroundColor(todo.groupColor.toInt())
        checkbox_todo.setChecked((parent as ListView).isItemChecked(position))

        view.setOnClickListener { //리스트뷰 눌렀을시 체크박스 선택
            click++
            when (click%2) {
                0 -> {
                    checkbox_todo.isChecked = false
                }
                1 -> {
                    checkbox_todo.isChecked = true
                }
            }
        }
        checkbox_todo.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                pos.add(position)
            } else {
                pos.remove(position)
            }
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