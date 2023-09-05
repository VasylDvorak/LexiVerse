package com.diplomproject.learningtogether.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "course")
data class CourseEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Long = 0,

    val name: String = "",

    @get: PropertyName("logo_url")
    @field: PropertyName("logo_url")
    @SerializedName("logo_url")
    val logoUrl: String? = null,

    @SerializedName("lessons")
    @Embedded
    val lessons: MutableList<LessonEntity> = mutableListOf()

) : Parcelable