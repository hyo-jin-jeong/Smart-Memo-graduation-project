package com.kakao.smartmemo.Data

import android.os.Parcel
import android.os.Parcelable

data class PlaceData(val place : String, val latitude : Double = 0.0, val longitude : Double = 0.0):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(place)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
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