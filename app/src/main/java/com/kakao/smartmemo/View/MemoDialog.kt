package com.kakao.smartmemo.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Adapter.MemoDialogAdapter
import com.kakao.smartmemo.Contract.MemoDialogContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Presenter.MemoDialogPresenter
import com.kakao.smartmemo.R

class MemoDialog(memo: MutableList<MemoData>) : Fragment(), MemoDialogContract.View {

    private lateinit var presenter: MemoDialogPresenter
    private lateinit var recyclerView: RecyclerView
    private var memo = memo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.memo_dialog, container, false)
        presenter = MemoDialogPresenter(this)
        var memoAdapter = MemoDialogAdapter(memo)
        recyclerView = view.findViewById<RecyclerView?>(R.id.alarm_settings_view) as RecyclerView
        recyclerView.adapter = memoAdapter
        presenter.setMemoDialogAdatperView(memoAdapter)
        presenter.setMemoDialogAdatperModel(memoAdapter)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        return view
    }

    override fun showMemoItem(position: Int) {
        val intent = Intent(view?.context, ShowMemo::class.java)
        intent.putExtra("memoData",memo[position])
        startActivity(intent)
    }

}
