package com.kakao.smartmemo.ApiConnect

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class CategoryResult : Parcelable {
    @SerializedName("meta")
    @Expose
    private var meta: Meta? = null

    @SerializedName("documents")
    @Expose
    private var documents: List<Document?>? = null
    fun getMeta(): Meta? {
        return meta
    }

    fun setMeta(meta: Meta?) {
        this.meta = meta
    }

    fun getDocuments(): List<Document?>? {
        return documents
    }

    fun setDocuments(documents: List<Document?>?) {
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
        documents = ArrayList<Document?>()
        parcel.readList(documents, Document::class.java.classLoader)
    }

    companion object {
        val CREATOR: Parcelable.Creator<CategoryResult?> =
            object : Parcelable.Creator<CategoryResult?> {
                override fun createFromParcel(source: Parcel): CategoryResult? {
                    return CategoryResult(source)
                }

                override fun newArray(size: Int): Array<CategoryResult?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
