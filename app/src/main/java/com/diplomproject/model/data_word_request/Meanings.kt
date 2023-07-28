package com.diplomproject.model.data_word_request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Meanings(
    @field:SerializedName("id") val id: Int? = 0,
    @field:SerializedName("translation") val translation: Translation? = Translation(),
    @field:SerializedName("imageUrl") val imageUrl: String? = "",
    @field:SerializedName("transcription") val transcription: String? = "",
    @field:SerializedName("soundUrl") val soundUrl: String? = ""
) : Parcelable
