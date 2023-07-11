package com.diplomproject.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataModel(
    @field:SerializedName("text") val text: String? = "",
    @field:SerializedName("meanings") val meanings: List<Meanings>? = listOf()
) : Parcelable
