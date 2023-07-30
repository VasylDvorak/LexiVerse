package com.diplomproject.learningtogether.domain.entities

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseEntity(
    val id: Long = 0,
    val name: String = "",
    @get: PropertyName("logo_url")
    @field: PropertyName("logo_url")
    @SerializedName("logo_url")
    val logoUrl: String? = null,
    val lessons: MutableList<LessonEntity> = mutableListOf()
) : Parcelable