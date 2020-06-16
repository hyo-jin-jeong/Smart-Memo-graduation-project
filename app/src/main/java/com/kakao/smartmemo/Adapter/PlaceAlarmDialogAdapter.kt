package com.kakao.smartmemo.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.TodoDialogAdapterContract
import com.kakao.smartmemo.Data.PlaceAlarmData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.alarm_list_item.view.*

class PlaceAlarmDialogAdapter(private var todo: MutableList<PlaceAlarmData>): RecyclerView.Adapter<PlaceAlarmDialogAdapter.DialogViewHolder>(), TodoDialogAdapterContract.View, TodoDialogAdapterContract.Model {

    //View Holder생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DialogViewHolder(parent)

    //Recycler View에 표시할 갯수
    override fun getItemCount(): Int = todo.size

    //View가 Bind되었을때의 설정
    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        todo[position].let {
            with(holder) {
                if (it.date == "[기본] 날짜 미설정") {
                    alarmDate.text = "날짜 없음"
                } else {
                    it.date = it.date.replace("년", "/")
                    it.date = it.date.replace(" ", "")
                    it.date = it.date.replace("월","/")
                    it.date = it.date.replace("일", " ")
                    alarmDate.text = it.date
                }
                alarmPlace.text = it.place
                alarmContent.text = it.content
            }
        }

    }

    inner class DialogViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.alarm_list_item, parent, false)) {
        var layout = itemView.alarm_list_item_layout
        val alarmPlace: TextView = itemView.textView_alarm_place
        val alarmDate: TextView = itemView.textView_date
        val alarmContent: TextView = itemView.textView_alarm_content
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getTodoPlaceAlarm() {

    }
}
