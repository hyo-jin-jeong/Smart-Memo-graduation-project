package com.kakao.smartmemo.Data

import android.os.Parcel
import android.os.Parcelable


data class PlaceData(var placeId: String = "", var place : String = "", var latitude : Double = 0.0,
                     var longitude : Double = 0.0, var id:String = "")
    :Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeId)
        parcel.writeString(place)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceData> {
        override fun createFromParcel(parcel: Parcel): PlaceData {
            return PlaceData(parcel)
        }

        override fun newArray(size: Int): Array<PlaceData?> {
            return arrayOfNulls(size)
        }
    }

}