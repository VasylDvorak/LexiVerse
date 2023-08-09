package com.diplomproject.learningtogether.domain.entities

import com.google.gson.annotations.SerializedName

data class SkyengMeaningEntity(

    val id: Int,

    @field:SerializedName("imageUrl")
    val imageUrl: String
)