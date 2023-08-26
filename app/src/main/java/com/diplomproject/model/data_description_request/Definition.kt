package com.diplomproject.model.data_description_request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Definition(
    @field:SerializedName("soundUrl") val soundUrl: String? = "",
    @field:SerializedName("text") val text: String? = ""
) : Parcelable