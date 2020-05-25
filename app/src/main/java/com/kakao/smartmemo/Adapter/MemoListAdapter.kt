package com.kakao.smartmemo.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.MemoAdapterContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.memo_list_view.view.*

class MemoListAdapter : RecyclerView.Adapter<MemoListAdapter.MainViewHolder>(), MemoAdapterContract.Model, MemoAdapterContract.View { //memoList RecycleView

    var items: MutableList<MemoData> = mutableListOf(MemoData("2020.3.2", "학교","기업조사"),
        MemoData("2020.3.12", "내메모","도서관 책반납"),MemoData("2020.2.20." , "여행","숙소예약"))
    override var onClickFunc: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                memoTitle.text = item.title
                memoContent.text = item.content
                memoDate.text =item.date
                memoBackground.setOnClickListener {
                    onClickFunc?.invoke(position)
                }
                when (item.title) {
                    "학교" -> {
                        memoBackground.setBackgroundColor(Color.parseColor("#FCE3FF"))
                    }
                    "내메모" -> {
                        memoBackground.setBackgroundColor(Color.parseColor("#FCECC0"))
                    }
                    "여행" -> {
                        memoBackground.setBackgroundColor(Color.parseColor("#AEC0F2"))
                    }
                }
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.memo_list_view, parent, false)) {
        val memoTitle: TextView = itemView.memo_title
        val memoContent: TextView = itemView.memo_content
        val memoDate: TextView = itemView.memo_date
        val memoBackground: CardView = itemView.memo_list_view
    }


    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getMemo(position: Int)= items[position]

    override fun deleteMemo() {

    }
}


