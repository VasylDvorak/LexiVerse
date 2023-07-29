package com.diplomproject.learningtogether.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LessonIdEntity(
    val courseId: Long,
    val lessonId: Long
) : Parcelable