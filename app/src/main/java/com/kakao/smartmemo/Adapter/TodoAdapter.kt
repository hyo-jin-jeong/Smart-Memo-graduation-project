package com.kakao.smartmemo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.smartmemo.Contract.TodoAdapterContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Model.TodoModel
import com.kakao.smartmemo.R
import com.kakao.smartmemo.View.TodoListActivity
import kotlinx.android.synthetic.main.todo_list_item.view.*
import kotlinx.android.synthetic.main.todolist_fragment.view.*

class TodoAdapter(val context: Context, private val todoList: ArrayList<TodoData>) : BaseAdapter(), TodoAdapterContract.Model, TodoAdapterContract.View {

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.todo_list_item, null)
        val todo = todoList[position]

        var checkedTodo = false
        view.textView_todo.text = todo.todoContent
        view.group_color.setBackgroundColor(Color.parseColor("#B2CCFF"))


//        view.textView_todo.setOnClickListener(View.OnClickListener { //todolist클릭했을시 설정 페이지(time_location_settings)로 넘어감.
//            val alarmSettingsIntent = Intent(it.context, TodoListActivity::class.java)
//            it.context.startActivity(alarmSettingsIntent)
//        })



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