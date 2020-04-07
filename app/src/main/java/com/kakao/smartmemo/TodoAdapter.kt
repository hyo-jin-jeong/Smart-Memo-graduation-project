package com.kakao.smartmemo

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.DTO.TodoDTO
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var datas: MutableList<TodoDTO> =
        mutableListOf(TodoDTO("약먹기"), TodoDTO("도서관 책 반납"))

    lateinit var textview : TextView
    lateinit var bellTodo : Button

    //View Holder생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    //Recycler View에 표시할 갯수
    override fun getItemCount(): Int = datas.size

    //View가 Bind되었을때의 설정
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        datas[position].let {
            with(holder) {
                textview=this.txt_todo
             /*   bellTodo=this.btn_todo
                checkBox= this.checkBox_todo*/
                textview.text = it.todoContent

            }
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            textview = it.findViewById(R.id.textView_todo) //취소선
            textview.setPaintFlags(textview.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)  //취소선 설정
            bellTodo = it.findViewById(R.id.btn_todo)
            /*if(checkBox.isChecked){
                textview.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG  //취소선 설정
            }
            else{
                textview.paintFlags = 0  //취소선 설정
            }*/
            bellTodo.setOnClickListener(View.OnClickListener {
                bellTodo.setBackgroundResource(R.drawable.bell_icon_on)
                val alarmSettingsIntent = Intent(it.context, TodoListActivity::class.java)
                Log.v("seyuuuun", "Intent")
                it.context.startActivity(alarmSettingsIntent)
                Log.v("seyuuuun", "Intent2")
            })


        })
    }

    //데이터들 업데이트
    fun setDataList(dataList: List<TodoDTO>?) {
        if (dataList == null) {
            return
        }
        this@TodoAdapter.datas = dataList.toMutableList()
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)) {
        val txt_todo = itemView.textView_todo
        val btn_todo = itemView.btn_todo
    }
}