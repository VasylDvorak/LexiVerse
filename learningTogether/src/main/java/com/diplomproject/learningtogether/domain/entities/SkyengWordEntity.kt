package com.diplomproject.learningtogether.domain.entities

import com.google.gson.annotations.SerializedName

data class SkyengWordEntity(

    val id: Int,

    @field:SerializedName("text")
    val text: String,

    val meanings: List<SkyengMeaningEntity>
)