package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Address : Parcelable {
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

    @SerializedName("region_3depth_h_name")
    @Expose
    var regionHName3: String? = null

    @SerializedName("h_code")
    @Expose
    var hCode: String? = null

    @SerializedName("b_code")
    @Expose
    var bCode: String? = null

    @SerializedName("mountain_yn")
    @Expose
    var mountainYN: String? = null

    @SerializedName("main_address_no")
    @Expose
    var mainAddressNo: String? = null

    @SerializedName("sub_address_no")
    @Expose
    var subAddressNo: String? = null

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
        dest.writeString(regionHName3)
        dest.writeString(hCode)
        dest.writeString(bCode)
        dest.writeString(mountainYN)
        dest.writeString(mainAddressNo)
        dest.writeString(subAddressNo)
        dest.writeString(x)
        dest.writeString(y)
    }

    constructor(parcel: Parcel) {
        addressName = parcel.readString()
        regionName1 = parcel.readString()
        regionName2 = parcel.readString()
        regionName3 = parcel.readString()
        regionHName3 = parcel.readString()
        hCode = parcel.readString()
        bCode = parcel.readString()
        mountainYN = parcel.readString()
        mainAddressNo = parcel.readString()
        subAddressNo = parcel.readString()
        x = parcel.readString()
        y = parcel.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<Address?> = object : Parcelable.Creator<Address?> {
            override fun createFromParcel(source: Parcel): Address? {
                return Address(source)
            }

            override fun newArray(size: Int): Array<Address?> {
                return arrayOfNulls(size)
            }
        }
    }
}