package com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.ApiConnect.BusProvider
import com.kakao.smartmemo.ApiConnect.Document
import com.kakao.smartmemo.R


class LocationAdapter(
    items: ArrayList<Document>,
    context: Context,
    searchView: SearchView,
    recyclerView: RecyclerView
) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {
    var context: Context = context
    var items: ArrayList<Document> = items
    var searchView: SearchView = searchView
    var recyclerView: RecyclerView = recyclerView
    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Document) {
        items.add(item)
    }

    fun clear() {
        items.clear()
    }

    override fun getItemId(position: Int): Long {
        return items[position].id!!.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): LocationViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_location, viewGroup, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LocationViewHolder,
        i: Int
    ) {
        val model: Document = items[i]
        holder.placeNameText.text = model.placeName
        holder.addressText.text = model.addressName
        holder.placeNameText.setOnClickListener {
            //searchView의 text는 바꾸되 query를 전송하지는 X
            searchView.setQuery(model.placeName, false)
            recyclerView.visibility = View.GONE
            BusProvider().getInstance().post(model)
        }
    }

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeNameText: TextView = itemView.findViewById(R.id.place_name)
        var addressText: TextView = itemView.findViewById(R.id.address)

    }

}