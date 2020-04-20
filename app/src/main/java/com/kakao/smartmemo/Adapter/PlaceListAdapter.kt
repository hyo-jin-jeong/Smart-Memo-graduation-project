package com.kakao.smartmemo.com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.kakao.smartmemo.Contract.TodoDateAdapterContract
import com.kakao.smartmemo.Contract.TodoPlaceAdapterContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.R

class PlaceListAdapter(val context: Context, val placeList: ArrayList<PlaceData>) : BaseAdapter(), TodoPlaceAdapterContract.View, TodoPlaceAdapterContract.Model {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.alarm_settings_list_item, null)
        val textView_place = view.findViewById<TextView>(R.id.textView_alarm_place_list)

        val Place = placeList[position]

        textView_place.text = Place.place

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

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

}