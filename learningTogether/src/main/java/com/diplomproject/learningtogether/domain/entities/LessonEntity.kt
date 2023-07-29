package com.diplomproject.learningtogether.domain.entities

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LessonEntity(
    val id: Long = 0,
    val name: String = "",
    @get: PropertyName("image_url")
    @field: PropertyName("image_url")
    @SerializedName("image_url")
    val imageUrl: String = "",
    @get: PropertyName("victory_image_url")
    @field: PropertyName("victory_image_url")
    @SerializedName("victory_image_url")
    val victoryImageUrl: String? = null,
    val tasks: MutableList<TaskEntity> = mutableListOf()
) : Parcelable