package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RoadAddress : Parcelable {
    @SerializedName("address_name")
    @Expose
    var addressName: String? = null

    @SerializedName("region_1depth_name")
    @Expose
    var regionName1: String? = null

    @SerializedName("region_2depth_name")
    @Expose
    var regionName2: String? = null

    @SerializedName("region_3depth_name")
    @Expose
    var regionName3: String? = null

    @SerializedName("road_name")
    @Expose
    var roadName: String? = null

    @SerializedName("underground_yn")
    @Expose
    var undergroundYN: String? = null

    @SerializedName("main_building_no")
    @Expose
    var mainBuildingNo: String? = null

    @SerializedName("sub_building_no")
    @Expose
    var subBuildingNo: String? = null

    @SerializedName("building_name")
    @Expose
    var buildingName: String? = null

    @SerializedName("zone_no")
    @Expose
    var zoneNo: String? = null

    @SerializedName("x")
    @Expose
    var x: String? = null

    @SerializedName("y")
    @Expose
    var y: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(addressName)
        dest.writeString(regionName1)
        dest.writeString(regionName2)
        dest.writeString(regionName3)
        dest.writeString(roadName)
        dest.writeString(undergroundYN)
        dest.writeString(mainBuildingNo)
        dest.writeString(subBuildingNo)
        dest.writeString(buildingName)
        dest.writeString(zoneNo)
        dest.writeString(x)
        dest.writeString(y)
    }

    constructor(parcel: Parcel) {
        addressName = parcel.readString()
        regionName1 = parcel.readString()
        regionName2 = parcel.readString()
        regionName3 = parcel.readString()
        roadName = parcel.readString()
        undergroundYN = parcel.readString()
        mainBuildingNo = parcel.readString()
        subBuildingNo = parcel.readString()
        buildingName = parcel.readString()
        zoneNo = parcel.readString()
        x = parcel.readString()
        y = parcel.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<RoadAddress?> = object : Parcelable.Creator<RoadAddress?> {
            override fun createFromParcel(source: Parcel): RoadAddress? {
                return RoadAddress(source)
            }

            override fun newArray(size: Int): Array<RoadAddress?> {
                return arrayOfNulls(size)
            }
        }
    }
}