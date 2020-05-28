package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DocumentAddress : Parcelable {
    @SerializedName("address_name")
    @Expose
    var addressName: String? = null

    @SerializedName("x")
    @Expose
    var x: String? = null

    @SerializedName("y")
    @Expose
    var y: String? = null

    @SerializedName("address_type")
    @Expose
    var addressType: String? = null

    @SerializedName("address")
    @Expose
    var address: Address? = null

    @SerializedName("road_address")
    @Expose
    var roadAddress: RoadAddress? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(addressName)
        dest.writeString(x)
        dest.writeString(y)
        dest.writeString(addressType)
        dest.writeParcelable(address, flags)
        dest.writeParcelable(roadAddress, flags)
    }

    constructor(parcel: Parcel) {
        addressName = parcel.readString()
        x = parcel.readString()
        y = parcel.readString()
        addressType = parcel.readString()
        address = parcel.readParcelable(Address::class.java.classLoader)
        roadAddress = parcel.readParcelable(RoadAddress::class.java.classLoader)
    }

    companion object {
        val CREATOR: Parcelable.Creator<DocumentAddress?> = object : Parcelable.Creator<DocumentAddress?> {
            override fun createFromParcel(source: Parcel): DocumentAddress? {
                return DocumentAddress(source)
            }

            override fun newArray(size: Int): Array<DocumentAddress?> {
                return arrayOfNulls(size)
            }
        }
    }
}