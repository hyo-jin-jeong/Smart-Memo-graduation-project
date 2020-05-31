package com.kakao.smartmemo.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.MemoAdapterContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.memo_list_view.view.*



class MemoListAdapter(private val memoList: MutableList<MemoData>) : RecyclerView.Adapter<MemoListAdapter.MainViewHolder>(), MemoAdapterContract.Model, MemoAdapterContract.View { //memoList RecycleView

    override var onClickFunc: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)

    override fun getItemCount(): Int = memoList.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        memoList[position].let { item ->
            with(holer) {
                groupName.text = item.groupName
                memoTitle.text = item.title
                memoContent.text = item.content
                memoDate.text = item.date
                memoBackground.isClickable = true
                memoBackground.setBackgroundColor(item.groupColor.toInt())
                memoBackground.setOnClickListener {
                    onClickFunc?.invoke(position)
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
        val groupName : TextView = itemView.group_name
    }


    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getMemo(position: Int)= position

    override fun deleteMemo() {

    }
}
