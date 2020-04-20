package com.kakao.smartmemo.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.MemoDialogAdapterContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.memo_list_view.view.*

class MemoDialogAdapter: RecyclerView.Adapter<MemoDialogAdapter.DialogViewHolder>(), MemoDialogAdapterContract.View, MemoDialogAdapterContract.Model {

    var datas: MutableList<MemoData> = mutableListOf(
        MemoData("2020.3.2", "학교","기업조사"),
        MemoData("2020.3.12", "내메모","도서관 책반납"), MemoData("2020.2.20." , "여행","숙소예약")
    )
    //View Holder생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DialogViewHolder(parent)

    //Recycler View에 표시할 갯수
    override fun getItemCount(): Int = datas.size

    //View가 Bind되었을때의 설정
    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        datas[position].let { item ->
            with(holder) {
                memoTitle.text = item.title
                memoContent.text = item.content
                memoDate.text =item.date
                if(item.title=="학교"){
                    memoBackground.setBackgroundColor(android.graphics.Color.parseColor("#FCE3FF"))
                }
                else if(item.title=="내메모"){
                    memoBackground.setBackgroundColor(android.graphics.Color.parseColor("#FCECC0"))
                }
                else if(item.title=="여행"){
                    memoBackground.setBackgroundColor(android.graphics.Color.parseColor("#AEC0F2"))
                }
            }
        }

    }

    //데이터들 업데이트
    fun setDataList(dataList: List<MemoData>?) {
        if (dataList == null) {
            return
        }
        this@MemoDialogAdapter.datas = dataList.toMutableList()
    }

    inner class DialogViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.memo_list_view, parent, false)) {

        val memoTitle = itemView.memo_title
        val memoContent = itemView.memo_content
        val memoDate = itemView.memo_date
        val memoBackground = itemView.memo_list_view

    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }
}
