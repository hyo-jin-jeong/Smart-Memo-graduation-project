package com.kakao.smartmemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.DTO.TodoDTO
import kotlinx.android.synthetic.main.alarm_list_item.view.*

class PlaceAlarmDialogAdapter: RecyclerView.Adapter<PlaceAlarmDialogAdapter.DialogViewHolder>() {
    private var datas: MutableList<TodoDTO> =
        mutableListOf(TodoDTO("약먹기"), TodoDTO("도서관 책 반납"))

    //View Holder생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DialogViewHolder(parent)

    //Recycler View에 표시할 갯수
    override fun getItemCount(): Int = datas.size

    //View가 Bind되었을때의 설정
    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        datas[position].let {
            with(holder) {
                txt_todo!!.text = it.todoContent
            }
        }

    }

    //데이터들 업데이트
    fun setDataList(dataList: List<TodoDTO>?) {
        if (dataList == null) {
            return
        }
        this@PlaceAlarmDialogAdapter.datas = dataList.toMutableList()
    }

    inner class DialogViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.alarm_dialog_list_item, parent, false)) {

        val txt_todo = itemView.textView_alarm_content
    }
}
