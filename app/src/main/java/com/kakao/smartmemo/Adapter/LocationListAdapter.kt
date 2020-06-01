package com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.kakao.smartmemo.Contract.LocationListAdapterContract
import com.kakao.smartmemo.R

class LocationListAdapter(val context: Context, private val placeList: ArrayList<String>): BaseAdapter(), LocationListAdapterContract.Model, LocationListAdapterContract.View {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.locaion_list_item, null)
        val placeTextView = view.findViewById<TextView>(R.id.textView_alarm_place_list)
        val deleteButton = view.findViewById<ImageButton>(R.id.btn_place_delete)

        placeTextView.text = placeList[position]

        //해보는 중이라 지우지 않겠습니다
//        deleteButton.setOnClickListener {
//            placeList.remove(placeList[position])
//        }

        return view
    }

    override fun getItem(position: Int): Any {
        return placeList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return placeList.size
    }

}