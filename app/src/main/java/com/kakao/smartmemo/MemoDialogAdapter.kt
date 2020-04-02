package com.kakao.smartmemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.DTO.TodoDTO
import kotlinx.android.synthetic.main.alarm_list_item.view.*

class MemoDialogAdapter: RecyclerView.Adapter<MemoDialogAdapter.DialogViewHolder>() {
    private var datas: MutableList<TodoDTO> =
        mutableListOf(TodoDTO("마우스 먼저 소독하기"), TodoDTO("웹프과제"), TodoDTO("랩실 신청"))

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
        this@MemoDialogAdapter.datas = dataList.toMutableList()
    }

    inner class DialogViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.memo_dialog_list_item, parent, false)) {

        val txt_todo = itemView.textView_alarm_content
    }
}
