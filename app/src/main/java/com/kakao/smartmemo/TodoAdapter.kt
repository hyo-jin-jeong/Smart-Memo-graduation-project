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

    private lateinit var textviewCancel : TextView
    private lateinit var bellTodo : ImageButton
    private lateinit var plusTodo : ImageButton
    private lateinit var editTextTodo: EditText

    //View Holder생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    //Recycler View에 표시할 갯수
    override fun getItemCount(): Int = datas.size

    //View가 Bind되었을때의 설정
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        datas[position].let {
            with(holder) {
                txt_todo.text = it.todoContent
            }
        }

        holder.itemView.setOnClickListener(View.OnClickListener {

            /*plusTodo = it.findViewById(R.id.btn_plus)
            plusTodo.setOnClickListener(View.OnClickListener {
                editTextTodo = it.editText_todo
                datas.add(TodoDTO(editTextTodo.toString()))
            })*/
            Log.v("seyuuun", it.toString())
            textviewCancel = it.findViewById(R.id.textView_todo) //취소선
            bellTodo = it.findViewById(R.id.btn_todo)
            bellTodo.setOnClickListener(View.OnClickListener {
                val alarmSettingsIntent = Intent(it.context, TodoListActivity::class.java)
                Log.v("seyuuuun", "Intent")
                it.context.startActivity(alarmSettingsIntent)
                Log.v("seyuuuun", "Intent2")
            })
            textviewCancel.setPaintFlags(textviewCancel.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)  //취소선 설정
            Toast.makeText(it.context, "Clicked :${datas.get(position).todoContent}", Toast.LENGTH_LONG).show()

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
        //val check_todo = itemView.checkBox_todo
        val txt_todo = itemView.textView_todo
        //val btn_todo = itemView.Btn_todo

        /*fun bind(data: TodoDTO) {
            txt_todo?.text = data.todoContent
        }*/

    }
}