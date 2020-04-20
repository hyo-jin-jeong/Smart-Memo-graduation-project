package com.kakao.smartmemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Adapter.MemoDialogAdapter
import com.kakao.smartmemo.Contract.MemoDialogContract
import com.kakao.smartmemo.Presenter.MemoDialogPresenter

class MemoDialog : Fragment(), MemoDialogContract.View {

    private lateinit var presenter: MemoDialogPresenter
    private lateinit var memoList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.memo_dialog, container, false)
        presenter = MemoDialogPresenter(this)
        var memoAdapter = MemoDialogAdapter()
        memoList = view.findViewById<RecyclerView?>(R.id.alarm_settings_view) as RecyclerView
        memoList.adapter = memoAdapter
        presenter.setMemoDialogAdatperView(memoAdapter)
        presenter.setMemoDialogAdatperModel(memoAdapter)
        memoList.layoutManager = LinearLayoutManager(view.context)

        return view
    }

}

