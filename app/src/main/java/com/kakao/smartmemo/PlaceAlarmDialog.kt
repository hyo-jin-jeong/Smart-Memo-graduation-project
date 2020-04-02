package com.kakao.smartmemo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceAlarmDialog : Fragment() {

    private lateinit var placeAlarmList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.place_alarm_dialog, container, false)

        placeAlarmList = view.findViewById<RecyclerView?>(R.id.alarm_settings_view) as RecyclerView
        placeAlarmList.adapter = PlaceAlarmDialogAdapter()
        placeAlarmList.layoutManager = LinearLayoutManager(view.context)

        return view
    }



}
