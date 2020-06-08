package com.kakao.smartmemo.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.MemoDeleteAdapterContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.memo_list_delete.view.*
import kotlinx.android.synthetic.main.memo_list_view.view.memo_content
import kotlinx.android.synthetic.main.memo_list_view.view.memo_date
import kotlinx.android.synthetic.main.memo_list_view.view.memo_title

class MemoListDeleteAdapter(private var memoList: MutableList<MemoData>) : RecyclerView.Adapter<MemoListDeleteAdapter.MainViewHolder>(), MemoDeleteAdapterContract.Model, MemoDeleteAdapterContract.View { //memoList RecycleView
    var deleteMemoList = mutableListOf<MemoData>()
    lateinit var holder:MainViewHolder
    var position:Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)

    override fun getItemCount(): Int = memoList.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        holder= holer
        memoList[position].let { item ->
            with(holer) {
                memoTitle.text = item.title
                memoContent.text = item.content
                memoDate.text =item.date
                GroupObject.groupColor[item.groupId]?.toInt()?.let {
                    memoBackground.setBackgroundColor(it)
                }
                checkboxTodo.setOnCheckedChangeListener { buttonView, _ ->
                    if(buttonView.isChecked){
                        deleteMemoList.add(memoList[position])
                    }
                }

            }

        }
    }
    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.memo_list_delete, parent, false)) {
        val memoTitle: TextView = itemView.memo_title
        val memoContent: TextView = itemView.memo_content
        val memoDate: TextView = itemView.memo_date
        val memoBackground: CardView = itemView.memo_list_delete
        var pos = arrayListOf<Int>()
        var checkboxTodo = itemView.findViewById(R.id.checkMemoDelete) as CheckBox
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getMemo() {

    }

    override fun deleteMemo(): MutableList<MemoData> {
        return deleteMemoList
    }
}


