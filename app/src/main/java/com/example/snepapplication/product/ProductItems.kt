package com.example.snepapplication.product

import android.os.Parcel
import android.os.Parcelable

data class ProductItems(var productName: String ="", var productRate: String ="",
                        var productDescription: String ="", var productLocation: String ="",
                        var productPrice: String ="", var productImage: String ="", var productCat:String=""):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeString(productRate)
        parcel.writeString(productDescription)
        parcel.writeString(productLocation)
        parcel.writeString(productPrice)
        parcel.writeString(productImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItems> {
        override fun createFromParcel(parcel: Parcel): ProductItems {
            return ProductItems(parcel)
        }

        override fun newArray(size: Int): Array<ProductItems?> {
            return arrayOfNulls(size)
        }
    }

}