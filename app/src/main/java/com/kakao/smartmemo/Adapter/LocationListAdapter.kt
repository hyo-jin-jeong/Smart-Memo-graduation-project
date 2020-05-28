package com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.kakao.smartmemo.Contract.LocationListAdapterContract
import com.kakao.smartmemo.R

class LocationListAdapter(val context: Context, private val placeList: ArrayList<String>): BaseAdapter(), LocationListAdapterContract.Model, LocationListAdapterContract.View {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.locaion_list_item, null)
        val textView_place = view.findViewById<TextView>(R.id.textView_alarm_place_list)

        textView_place.text = placeList[position]

//        var deleteBtn = view.findViewById<Button>(R.id.btn_place_delete)
//        deleteBtn.setOnClickListener {
//            var selectedPlace = it.
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