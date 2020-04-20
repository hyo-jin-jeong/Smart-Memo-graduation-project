package com.kakao.smartmemo

import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.View.MainActivity
import com.kakao.smartmemo.com.kakao.smartmemo.Adapter.AlarmAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class TimeAlarmFragment : Fragment(){

    lateinit var AlarmTime : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.time_alarm_fragment, container, false)

        AlarmTime = view.findViewById(R.id.alarm_settings_time_view) as RecyclerView
        AlarmTime.adapter =
            AlarmAdapter()
        AlarmTime.layoutManager = LinearLayoutManager(view.context)

        val placeView = inflater.inflate(R.layout.activity_main, container, false)
        val myToolbar = placeView.toolbar
        myToolbar.title = "알람관리"

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="시간알람설정"
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_alarm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            //시간알람관리를 눌렀을 때
            R.id.action_time_alarm -> {
                (activity as MainActivity).toolbar.title=item.title
                //val timeIntent = Intent(context, TimeAlarmFragment::class.java)
                //startActivity(timeIntent)
                return true
            }
            //장소알람관리를 눌렀을 때
            R.id.action_place_alarm -> {
                (activity as MainActivity).toolbar.title=item.title
                //val placeIntent = Intent(context, PlaceAlarmFragment::class.java)
                //startActivity(placeIntent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}