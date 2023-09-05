package com.diplomproject.learningtogether.domain.repos

import com.diplomproject.learningtogether.domain.entities.LessonIdEntity

interface FavoriteLessonsRepo {
    fun addFavorite(lessonId: LessonIdEntity)
    fun removeEntity(lessonId: LessonIdEntity)
    fun getFavorites(): List<LessonIdEntity>
    fun isFavorite(courseId: Long, lessonId: Long): Boolean
}