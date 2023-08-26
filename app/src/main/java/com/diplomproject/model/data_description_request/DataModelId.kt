package com.diplomproject.model.data_description_request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModelId(
    @field:SerializedName("definition") val definition: Definition? = Definition(),
    @field:SerializedName("examples") val examples: List<Example>? = listOf(),
) : Parcelable