package com.diplomproject.learningtogether.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task")
data class TaskEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Long = 0,

    val task: String = "",

    @get: PropertyName("task_image_url")
    @field: PropertyName("task_image_url")
    @SerializedName("task_image_url")
    var taskImageUrl: String? = null,

    @get: PropertyName("variants_answer")
    @field: PropertyName("variants_answer")
    @SerializedName("variants_answer")
    val variantsAnswer: List<String> = emptyList(),

    @get: PropertyName("right_answer")
    @field: PropertyName("right_answer")
    @SerializedName("right_answer")
    val rightAnswer: String = "",

    val level: Int = 0,

    val favourites: Boolean = false

) : Parcelable