package com.kakao.smartmemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.memo_list_view.view.*

class MemoListAdapter : RecyclerView.Adapter<MemoListAdapter.MainViewHolder>() { //memoList RecycleView

    var items: MutableList<MemoData> = mutableListOf(MemoData("Title1", "Contdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddent1"),
        MemoData("Title2", "Content2"),MemoData("Title3", "Content3"))

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                memoTitle.text = item.title
                memoContent.text = item.content
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.memo_list_view, parent, false)) {
        val memoTitle = itemView.memo_title
        val memoContent = itemView.memo_content
    }
}

