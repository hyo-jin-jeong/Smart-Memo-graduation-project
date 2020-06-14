package com.kakao.smartmemo.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.MemoDialogAdapterContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.memo_list_view.view.*

class MemoDialogAdapter(memo: MutableList<MemoData>): RecyclerView.Adapter<MemoDialogAdapter.DialogViewHolder>(), MemoDialogAdapterContract.View, MemoDialogAdapterContract.Model {

    private var datas: MutableList<MemoData> = memo
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
                FolderObject.folderColor[item.groupId]?.toInt()?.let {
                    memoBackground.setCardBackgroundColor(
                        it
                    )
                }
            }
        }

    }

    fun setDataList(dataList: List<MemoData>?) {
        if (dataList == null) {
            return
        }
        this@MemoDialogAdapter.datas = dataList.toMutableList()
    }

    inner class DialogViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.memo_list_view, parent, false)) {

        val memoTitle: TextView = itemView.memo_title
        val memoContent: TextView = itemView.memo_content
        val memoDate: TextView = itemView.memo_date
        val memoBackground: CardView = itemView.memo_list_view

    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getMemo() {

    }
}
