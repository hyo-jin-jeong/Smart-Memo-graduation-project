package com.kakao.smartmemo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Adapter.PlaceAlarmDialogAdapter
import com.kakao.smartmemo.Contract.TodoDialogContract
import com.kakao.smartmemo.Presenter.TodoDialogPresenter

class PlaceAlarmDialog : Fragment(), TodoDialogContract.View {

    private lateinit var presenter : TodoDialogPresenter
    private lateinit var placeAlarmList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.place_alarm_dialog, container, false)

        presenter = TodoDialogPresenter(this)
        var todoDialogAdapter =  PlaceAlarmDialogAdapter()
        placeAlarmList = view.findViewById<RecyclerView?>(R.id.alarm_settings_view) as RecyclerView
        placeAlarmList.adapter = todoDialogAdapter
        presenter.setTodoDialogAdapterView(todoDialogAdapter)
        presenter.setTodoDialogAdapterModel(todoDialogAdapter)
        placeAlarmList.layoutManager = LinearLayoutManager(view.context)

        return view
    }



}
