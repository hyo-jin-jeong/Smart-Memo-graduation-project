package com.kakao.smartmemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.DTO.PlaceAlarmDTO
import kotlinx.android.synthetic.main.alarm_list_item.view.*

class PlaceAlarmDialogAdapter: RecyclerView.Adapter<PlaceAlarmDialogAdapter.DialogViewHolder>() {

    var data:MutableList<PlaceAlarmDTO> = mutableListOf(PlaceAlarmDTO("한성대학교", "2020.03.14", "도서관 책 "), PlaceAlarmDTO("녹십자약국", "2020.03.15", "마스크 사기"))
    //View Holder생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DialogViewHolder(parent)

    //Recycler View에 표시할 갯수
    override fun getItemCount(): Int = data.size

    //View가 Bind되었을때의 설정
    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        data[position].let {
            with(holder) {
                alarm_place.text = it.place
                alarm_date.text = it.date
                alarm_content.text = it.content
            }
        }

    }

    //데이터들 업데이트
    fun setDataList(dataList: List<PlaceAlarmDTO>?) {
        if (dataList == null) {
            return
        }
        this@PlaceAlarmDialogAdapter.data = dataList.toMutableList()
    }

    inner class DialogViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.alarm_list_item, parent, false)) {

        val alarm_place = itemView.textView_alarm_place
        val alarm_date = itemView.textView_date
        val alarm_content = itemView.textView_alarm_content
    }
}
