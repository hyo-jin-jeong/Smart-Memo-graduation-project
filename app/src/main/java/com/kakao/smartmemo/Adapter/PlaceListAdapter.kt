package com.kakao.smartmemo.com.kakao.smartmemo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.kakao.smartmemo.Contract.TodoPlaceAdapterContract
import com.kakao.smartmemo.Data.PlaceData
import com.kakao.smartmemo.R

class PlaceListAdapter(val context: Context, val placeList: ArrayList<PlaceData>) : BaseAdapter(), TodoPlaceAdapterContract.View, TodoPlaceAdapterContract.Model {

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.alarm_settings_list_item, null)
        val textViewPlace = view.findViewById<TextView>(R.id.textView_alarm_place_list)
        val deletePlace = view.findViewById(R.id.btn_place_delete) as ImageButton

        val place = placeList[position]

        textViewPlace.text = place.place

        deletePlace.setOnClickListener {
            placeList.remove(getItem(position))
            this.notifyAdapter()
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return placeList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return placeList.size
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getTodoPlace() {

    }

    override fun deletePlace() {

    }

    override fun getList(): ArrayList<PlaceData> {
        return placeList
    }
}