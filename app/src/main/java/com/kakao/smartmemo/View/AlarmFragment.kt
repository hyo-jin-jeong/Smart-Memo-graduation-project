package com.kakao.smartmemo.View

import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.AlarmContract
import com.kakao.smartmemo.Presenter.AlarmPresenter
import com.kakao.smartmemo.R
import com.kakao.smartmemo.Adapter.AlarmAdapter
import kotlinx.android.synthetic.main.activity_main.*

class AlarmFragment : Fragment(), AlarmContract.View {

    lateinit var Alarm : RecyclerView
    lateinit var presenter : AlarmContract.Presenter
    lateinit var myAdapter: AlarmAdapter

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
        myAdapter = AlarmAdapter()
        myAdapter.setMode(0)
        myAdapter.initData()
        Alarm.adapter = myAdapter
        Alarm.layoutManager = LinearLayoutManager(activity)

        presenter.setAlarmAdapterModel(myAdapter)
        presenter.setAlarmAdapterView(myAdapter)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="장소 알람 관리"
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_alarm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            //장소알람관리를 눌렀을 때
            R.id.action_place_alarm -> {
                (activity as MainActivity).toolbar.title=item.title
                myAdapter.setMode(0)
                myAdapter.notifyAdapter()
                return true
            }
            //시간알람관리를 눌렀을 때
            R.id.action_time_alarm -> {
                (activity as MainActivity).toolbar.title=item.title
                myAdapter.setMode(1)
                myAdapter.notifyAdapter()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}