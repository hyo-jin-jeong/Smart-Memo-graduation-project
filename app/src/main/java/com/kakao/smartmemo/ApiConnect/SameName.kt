package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class SameName : Parcelable {
    @SerializedName("region")
    @Expose
    var region: List<Any?>? = null

    @SerializedName("keyword")
    @Expose
    var keyword: String? = null

    @SerializedName("selected_region")
    @Expose
    var selectedRegion: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(region)
        dest.writeString(keyword)
        dest.writeString(selectedRegion)
    }

    constructor(parcel: Parcel) {
        region = ArrayList()
        parcel.readList(region, Any::class.java.classLoader)
        keyword = parcel.readString()
        selectedRegion = parcel.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<SameName?> = object : Parcelable.Creator<SameName?> {
            override fun createFromParcel(source: Parcel): SameName? {
                return SameName(source)
            }

            override fun newArray(size: Int): Array<SameName?> {
                return arrayOfNulls(size)
            }
        }
    }
}
