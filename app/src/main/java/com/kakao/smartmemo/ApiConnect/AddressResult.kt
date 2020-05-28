package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class AddressResult : Parcelable {
    @SerializedName("meta")
    @Expose
    private var meta: Meta? = null

    @SerializedName("documents")
    @Expose
    private var documents: List<DocumentAddress?>? = null
    fun getMeta(): Meta? {
        return meta
    }

    fun setMeta(meta: Meta?) {
        this.meta = meta
    }

    fun getDocuments(): List<DocumentAddress?>? {
        return documents
    }

    fun setDocuments(documents: List<DocumentAddress?>?) {
        this.documents = documents
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(meta, flags)
        dest.writeList(documents)
    }

    constructor(parcel: Parcel) {
        meta = parcel.readParcelable(Meta::class.java.classLoader)
        documents = ArrayList<DocumentAddress?>()
        parcel.readList(documents, DocumentAddress::class.java.classLoader)
    }

    companion object {
        val CREATOR: Parcelable.Creator<AddressResult?> =
            object : Parcelable.Creator<AddressResult?> {
                override fun createFromParcel(source: Parcel): AddressResult? {
                    return AddressResult(source)
                }

                override fun newArray(size: Int): Array<AddressResult?> {
                    return arrayOfNulls(size)
                }
            }
    }
}