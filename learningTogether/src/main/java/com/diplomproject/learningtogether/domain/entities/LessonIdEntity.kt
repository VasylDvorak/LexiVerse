package com.diplomproject.learningtogether.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "lesson_id",
    primaryKeys = ["course_id", "lesson_id"]
)

data class LessonIdEntity(

    @ColumnInfo(name = "course_id")
    val courseId: Long,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long

) : Parcelable