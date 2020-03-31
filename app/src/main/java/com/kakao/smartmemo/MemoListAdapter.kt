package com.kakao.smartmemo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.DTO.MemoData
import kotlinx.android.synthetic.main.memo_list_view.view.*

class MemoListAdapter : RecyclerView.Adapter<MemoListAdapter.MainViewHolder>() { //memoList RecycleView

    var items: MutableList<MemoData> = mutableListOf(MemoData("2020.3.2", "학교","기업조사"),
        MemoData("2020.3.12", "내메모","도서관 책반납"),MemoData("2020.2.20." , "여행","숙소예약"))

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                memoTitle.text = item.title
                memoContent.text = item.content
                memoDate.text =item.date
                if(item.title=="학교"){
                    memoBackground.setBackgroundColor(Color.parseColor("#FCE3FF"))
                }
                else if(item.title=="내메모"){
                    memoBackground.setBackgroundColor(Color.parseColor("#FCECC0"))
                }
                else if(item.title=="여행"){
                    memoBackground.setBackgroundColor(Color.parseColor("#AEC0F2"))
                }
            }

        }

    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.memo_list_view, parent, false)) {
        val memoTitle = itemView.memo_title
        val memoContent = itemView.memo_content
        val memoDate = itemView.memo_date
        val memoBackground = itemView.memo_list_view

    }
}


