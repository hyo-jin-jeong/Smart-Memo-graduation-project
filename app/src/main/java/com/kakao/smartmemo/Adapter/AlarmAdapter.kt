package com.kakao.smartmemo.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.AlarmAdapterContract
import com.kakao.smartmemo.Data.PlaceAlarmData
import com.kakao.smartmemo.Data.TimeAlarmData
import com.kakao.smartmemo.Model.PlaceAlarmModel
import com.kakao.smartmemo.Model.TimeAlarmModel
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.alarm_list_item.view.*

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.ViewHolder>(), AlarmAdapterContract.View, AlarmAdapterContract.Model {

    lateinit var placeModel : PlaceAlarmModel
    lateinit var timeModel: TimeAlarmModel

    lateinit var placeData : MutableList<PlaceAlarmData>
    lateinit var timeData : MutableList<TimeAlarmData>

    var data:MutableList<PlaceAlarmData> = mutableListOf(PlaceAlarmData("한성대학교", "2020.03.14", "도서관 책 "), PlaceAlarmData("녹십자약국", "2020.03.15", "마스크 사기"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let {
            with(holder) {
                alarm_place.text = it.place
                alarm_date.text = it.date
                alarm_content.text = it.content
            }
        }
    }

    override fun notifyAdapter () {
        notifyDataSetChanged()
    }

    //데이터들 업데이트
    fun setDataList(dataList: List<PlaceAlarmData>?) {
        if (dataList == null) {
            return
        }
        this@AlarmAdapter.data = dataList.toMutableList()
    }

    inner class ViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.alarm_list_item, parent, false)) {
        //스위치 온클릭 리스너 달기
        val alarm_place = itemView.textView_alarm_place
        val alarm_date = itemView.textView_date
        val alarm_content = itemView.textView_alarm_content
    }

    override fun setTimeAlarmModel(model: TimeAlarmModel) {
        timeModel = model
    }

    override fun setPlaceAlarmModel(model: PlaceAlarmModel) {
        placeModel = model
    }
    fun initTimeAlarm() {

    }
}