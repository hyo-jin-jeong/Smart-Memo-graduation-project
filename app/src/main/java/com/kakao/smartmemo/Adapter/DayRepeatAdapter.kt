package com.kakao.smartmemo.com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.TodoDateAdapterContract
import com.kakao.smartmemo.Data.DayData
import com.kakao.smartmemo.R

import kotlinx.android.synthetic.main.repeat_day_item.view.*

class DayRepeatAdapter(val context: Context, val dayList: MutableList<DayData>): RecyclerView.Adapter<DayRepeatAdapter.ViewHolder>(), TodoDateAdapterContract.View, TodoDateAdapterContract.Model{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount(): Int = dayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dayList[position].let {
            with(holder) {
                day_btn.text = it.day
            }
        }

        holder.day_btn.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, holder.day_btn.text, Toast.LENGTH_SHORT).show()

        })
    }

    inner class ViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.repeat_day_item, parent, false)) {
        val day_btn = itemView.btn_day_repeat
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }


}