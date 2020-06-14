package com.kakao.smartmemo.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.TodoDialogAdapterContract
import com.kakao.smartmemo.Data.PlaceAlarmData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.alarm_list_item.view.*

class PlaceAlarmDialogAdapter(todo: MutableList<PlaceAlarmData>): RecyclerView.Adapter<PlaceAlarmDialogAdapter.DialogViewHolder>(), TodoDialogAdapterContract.View, TodoDialogAdapterContract.Model {

    var data:MutableList<PlaceAlarmData> = todo
    //View Holder생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DialogViewHolder(parent)

    //Recycler View에 표시할 갯수
    override fun getItemCount(): Int = data.size

    //View가 Bind되었을때의 설정
    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        data[position].let {
            with(holder) {
                alarmPlace.text = it.place
                alarmDate.text = it.date
                alarmContent.text = it.content
                switch.isChecked = it.onoff
            }
        }

    }

    //데이터들 업데이트
    fun setDataList(dataList: List<PlaceAlarmData>?) {
        if (dataList == null) {
            return
        }
        this@PlaceAlarmDialogAdapter.data = dataList.toMutableList()
    }

    inner class DialogViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.alarm_list_item, parent, false)) {

        val alarmPlace: TextView = itemView.textView_alarm_place
        val alarmDate: TextView = itemView.textView_date
        val alarmContent: TextView = itemView.textView_alarm_content
        val switch: Switch = itemView.switch_alarm_settings
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getTodoPlaceAlarm() {

    }
}
