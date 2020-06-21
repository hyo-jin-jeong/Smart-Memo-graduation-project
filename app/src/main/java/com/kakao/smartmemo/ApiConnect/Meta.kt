package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meta : Parcelable {
    @SerializedName("same_name")
    @Expose
    private var sameName: SameName? = null

    @SerializedName("pageable_count")
    @Expose
    var pageableCount: Int? = null

    @SerializedName("total_count")
    @Expose
    var totalCount: Int? = null

    @SerializedName("is_end")
    @Expose
    var isEnd: Boolean? = null

    fun getSameName(): SameName? {
        return sameName
    }

    fun setSameName(sameName: SameName?) {
        this.sameName = sameName
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(sameName, flags)
        dest.writeValue(pageableCount)
        dest.writeValue(totalCount)
        dest.writeValue(isEnd)
    }

    constructor(parcel: Parcel) {
        sameName = parcel.readParcelable(SameName::class.java.classLoader)
        pageableCount = parcel.readValue(Int::class.java.classLoader) as Int
        totalCount = parcel.readValue(Int::class.java.classLoader) as Int
        isEnd = parcel.readValue(Boolean::class.java.classLoader) as Boolean
    }

    companion object {
        val CREATOR: Parcelable.Creator<Meta?> = object : Parcelable.Creator<Meta?> {
            override fun createFromParcel(source: Parcel): Meta? {
                return Meta(source)
            }

            override fun newArray(size: Int): Array<Meta?> {
                return arrayOfNulls(size)
            }
        }
    }
}