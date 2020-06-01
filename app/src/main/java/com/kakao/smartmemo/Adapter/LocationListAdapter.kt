package com.kakao.smartmemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kakao.smartmemo.Contract.LocationListAdapterContract
import com.kakao.smartmemo.R
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapView

class LocationListAdapter(context: Context,
                          items: ArrayList<String>,
                          mapItems: ArrayList<MapPOIItem>,
                          aroundItems: ArrayList<MapPOIItem>,
    mapView: MapView): RecyclerView.Adapter<LocationListAdapter.LocationListViewHolder>(), LocationListAdapterContract.Model, LocationListAdapterContract.View {
    var context: Context = context
    var items: ArrayList<String> = items
    var mapItems: ArrayList<MapPOIItem> = mapItems
    var aroundItems: ArrayList<MapPOIItem> = aroundItems
    var mapView: MapView = mapView

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): LocationListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.locaion_list_item, viewGroup, false)
        return LocationListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: String) {
        items.add(item)
    }

    fun clear() {
        items.clear()
    }

    override fun onBindViewHolder(holder: LocationListViewHolder, position: Int) {
        holder.placeTextView.text = items[position]
        holder.deleteButton.setOnClickListener {
            mapItems[position].markerType = MapPOIItem.MarkerType.BluePin
            items.remove(items[position])
            mapItems.remove(mapItems[position])
            this.notifyDataSetChanged()
            allMapItemShow()
        }
    }


    inner class LocationListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeTextView: TextView = itemView.findViewById<TextView>(R.id.textView_alarm_place_list)
        var deleteButton: ImageButton = itemView.findViewById<ImageButton>(R.id.btn_place_delete)

    }

    private fun allMapItemShow() {
        mapView.removeAllPOIItems()
        for (mapItem in mapItems) {
            mapView.addPOIItem(mapItem)
        }
        for (aroundMapItem in aroundItems) {
            mapView.addPOIItem(aroundMapItem)
        }
    }

}