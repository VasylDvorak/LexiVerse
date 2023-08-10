package com.diplomproject.learningtogether.domain.entities

import com.google.gson.annotations.SerializedName

data class SkyengWordEntity(

    @field:SerializedName("text")
    val text: String,

    @field:SerializedName("meanings")
    val meanings: List<SkyengMeaningEntity>
)