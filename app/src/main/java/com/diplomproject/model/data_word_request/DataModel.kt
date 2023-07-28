package com.diplomproject.model.data_word_request

import android.os.Parcelable
import com.diplomproject.model.data_description_request.Example
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataModel(
    @field:SerializedName("id") val id: Int? = 0,
    @field:SerializedName("text") val text: String? = "",
    @field:SerializedName("meanings") val meanings: List<Meanings>? = listOf(),
    var exampleDataModel: List<Example>? = listOf()
) : Parcelable
