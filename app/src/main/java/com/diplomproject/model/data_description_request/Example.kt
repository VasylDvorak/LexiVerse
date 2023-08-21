package com.diplomproject.model.data_description_request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Example(
    @field:SerializedName("soundUrl") var soundUrl: String? = "",
    @field:SerializedName("text") var text: String? = "",
    var testEnglishSamples: MutableList<TestEnglishSample>? = mutableListOf(),
    var imageUrl: String? = ""
) : Parcelable