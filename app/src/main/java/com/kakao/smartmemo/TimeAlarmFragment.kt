package com.kakao.smartmemo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        AlarmTime.adapter = AlarmAdapter()
        AlarmTime.layoutManager = LinearLayoutManager(view.context)

        val placeView = inflater.inflate(R.layout.activity_main, container, false)
        val myToolbar = placeView.toolbar
        myToolbar.title = "알람관리"

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        //return super.onCreateOptionsMenu(menu);
        super.onCreateOptionsMenu(menu, menuInflater);
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_alarm, menu)
        menu?.getItem(1)?.isChecked = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item?.itemId) {
            //android.R.id.home -> {
//                mDrawerLayout!!.openDrawer(GravityCompat.START)
//                true
//            }
            //시간알람관리를 눌렀을 때
            R.id.action_time_alarm -> {
                //val timealarmIntent = Intent(context, TimeAlarmActivity::class.java)
                //startActivity(timealarmIntent)
                return true
            }
            //장소알람관리를 눌렀을 때
            R.id.action_place_alarm -> {
                //val placealarmIntent =Intent(context, PlaceAlarmActivity::class.java)
                //startActivity(placealarmIntent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}