package com.kakao.smartmemo

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class PlaceAlarmFragment : Fragment() {

    lateinit var Alarm : RecyclerView

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

        Alarm = view.findViewById(R.id.alarm_settings_view) as RecyclerView
        Alarm.adapter = AlarmAdapter()
        Alarm.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="알람설정"
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_alarm, menu)
        menu?.getItem(1)?.isChecked = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            //시간알람관리를 눌렀을 때
            R.id.action_time_alarm -> {
                item.isChecked = !item.isChecked
                when(item.isChecked) {
                    true -> Toast.makeText(view?.context, item.title, Toast.LENGTH_SHORT).show()
                }
                true
            }
            //장소알람관리를 눌렀을 때
            R.id.action_place_alarm -> {
                item.isChecked = !item.isChecked
                when(item.isChecked) {
                    true -> Toast.makeText(view?.context, item.title, Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}