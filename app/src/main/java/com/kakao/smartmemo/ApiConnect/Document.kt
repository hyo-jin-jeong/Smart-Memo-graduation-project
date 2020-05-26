package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Document : Parcelable {
    @SerializedName("place_name")
    @Expose
    var placeName: String? = null

    @SerializedName("distance")
    @Expose
    var distance: String? = null

    @SerializedName("place_url")
    @Expose
    var placeUrl: String? = null

    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null

    @SerializedName("address_name")
    @Expose
    var addressName: String? = null

    @SerializedName("road_address_name")
    @Expose
    var roadAddressName: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("category_group_code")
    @Expose
    var categoryGroupCode: String? = null

    @SerializedName("category_group_name")
    @Expose
    var categoryGroupName: String? = null

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
        dest.writeString(placeName)
        dest.writeString(distance)
        dest.writeString(placeUrl)
        dest.writeString(categoryName)
        dest.writeString(addressName)
        dest.writeString(roadAddressName)
        dest.writeString(id)
        dest.writeString(phone)
        dest.writeString(categoryGroupCode)
        dest.writeString(categoryGroupName)
        dest.writeString(x)
        dest.writeString(y)
    }

    constructor(parcel: Parcel) {
        placeName = parcel.readString()
        distance = parcel.readString()
        placeUrl = parcel.readString()
        categoryName = parcel.readString()
        addressName = parcel.readString()
        roadAddressName = parcel.readString()
        id = parcel.readString()
        phone = parcel.readString()
        categoryGroupCode = parcel.readString()
        categoryGroupName = parcel.readString()
        x = parcel.readString()
        y = parcel.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<Document?> = object : Parcelable.Creator<Document?> {
            override fun createFromParcel(source: Parcel): Document? {
                return Document(source)
            }

            override fun newArray(size: Int): Array<Document?> {
                return arrayOfNulls(size)
            }
        }
    }
}