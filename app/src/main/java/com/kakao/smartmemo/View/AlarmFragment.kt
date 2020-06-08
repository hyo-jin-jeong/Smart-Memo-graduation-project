package com.kakao.smartmemo.View

import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.AlarmContract
import com.kakao.smartmemo.Presenter.AlarmPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Adapter.AlarmAdapter
import kotlinx.android.synthetic.main.activity_main.*

class AlarmFragment : Fragment(), AlarmContract.View {

    private lateinit var Alarm : RecyclerView
    private lateinit var presenter : AlarmContract.Presenter
    private lateinit var myAdapter: AlarmAdapter
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.place_alarm_fragment, container, false)
        presenter = AlarmPresenter(this)
        Alarm = view.findViewById(R.id.alarm_settings_view) as RecyclerView
        radioGroup = view.findViewById(R.id.radioGroup) as RadioGroup
        myAdapter = AlarmAdapter()
        myAdapter.setMode(0)
        myAdapter.initData()
        Alarm.adapter = myAdapter
        Alarm.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        radioGroup.check(R.id.placeAlarmRadio)

        presenter.setAlarmAdapterModel(myAdapter)
        presenter.setAlarmAdapterView(myAdapter)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.placeAlarmRadio -> {
                    (activity as MainActivity).toolbar.title="장소 알람 관리"
                    myAdapter.setMode(0)
                    myAdapter.notifyAdapter()
                }
                R.id.timeAlarmRadio -> {
                    (activity as MainActivity).toolbar.title="시간 알람 관리"
                    myAdapter.setMode(1)
                    myAdapter.notifyAdapter()
                }
            }
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="장소 알람 관리"
    }
}