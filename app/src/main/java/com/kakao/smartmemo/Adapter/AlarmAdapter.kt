package com.kakao.smartmemo.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.AlarmAdapterContract
import com.kakao.smartmemo.Data.PlaceAlarmData
import com.kakao.smartmemo.Data.TimeAlarmData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.alarm_list_item.view.*

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.ViewHolder>(), AlarmAdapterContract.View, AlarmAdapterContract.Model {
    var mode: Int? = null

    lateinit var placeData : MutableList<PlaceAlarmData>
    lateinit var timeData : MutableList<TimeAlarmData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount(): Int {
        return when(mode) {
            0 -> placeData.size
            1 -> timeData.size
            else -> 0
        }
    }

    fun initData() {
        //여기다 임의로 data 넣어줬습니다. 이것을 수정해서 DB 연결해주세용
        placeData = mutableListOf(PlaceAlarmData("한성대학교", "2020.03.14", "도서관 책 "), PlaceAlarmData("녹십자약국", "2020.03.15", "마스크 사기"))
        timeData = mutableListOf(TimeAlarmData("오전 9:00", "월요일 마다", "약 먹기"))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(mode) {
            0 -> {
                placeData[position].let {
                    with(holder) {
                        alarm_place.text = it.place
                        alarm_date.text = it.date
                        alarm_content.text = it.content
                    }
                }
            }
            1  -> {
                timeData[position].let {
                    with(holder) {
                        alarm_place.text = it.time
                        alarm_date.text = it.date
                        alarm_content.text = it.content
                    }
                }

            }
        }

        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true ->  {

                }
                false -> {

                }
            }
        }


    }

    override fun notifyAdapter () {
        notifyDataSetChanged()
    }

    //데이터들 업데이트
    fun setDataList(dataList: List<PlaceAlarmData>?) {
        //  현재는 장소알림으로 설정. 경우의 수 나눠서 시간알림도 설정해야
        if (dataList == null) {
            return
        }
        this@AlarmAdapter.placeData = dataList.toMutableList()
    }

    inner class ViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.alarm_list_item, parent, false)) {
        val alarm_place: TextView = itemView.textView_alarm_place
        val alarm_date: TextView = itemView.textView_date
        val alarm_content: TextView = itemView.textView_alarm_content

        val switch: Switch = itemView.switch_alarm_settings
    }

    fun setMode(mode: Int) {
        this.mode = mode
        Log.i("check!", "current mode :$mode")
    }

    override fun addTimeAlarm() {

    }

    override fun addPlaceAlarm() {

    }

    override fun updateTimeAlarm() {

    }

    override fun updatePlaceAlarm() {

    }

    override fun deleteTimeAlarm() {

    }

    override fun deletePlaceAlarm() {

    }

}