package com.kakao.smartmemo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ListView
import com.kakao.smartmemo.Contract.TodoDeleteAdapterContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.todo_list_delete.view.*

class TodoDeleteAdapter(val context: Context, private val todoList: MutableList<TodoData>) : BaseAdapter(), TodoDeleteAdapterContract.Model, TodoDeleteAdapterContract.View {

    var pos = hashMapOf<Int, TodoData>()
    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list_delete, null)
        val todo = todoList[position]
        var count = 0

        view.delete_group_name.text = GroupObject.groupInfo[todo.groupId]
        view.textView_todo.text = todo.title

        GroupObject.groupColor[todo.groupId]?.toInt().let {
            if (it != null) {
                view.group_color.setBackgroundColor(it)
            }
        }

        view.checkDelete.setOnClickListener { //리스트뷰 눌렀을시 체크박스 선택
            if (count % 2 == 1) {
                it.checkDelete.isChecked = false
                pos.remove(position)

            } else {
                it.checkDelete.isChecked = true
                pos.put(position, todoList[position])
            }
            count++
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

    fun selectedTodo() : HashMap<Int, TodoData>{
        return pos
    }

}