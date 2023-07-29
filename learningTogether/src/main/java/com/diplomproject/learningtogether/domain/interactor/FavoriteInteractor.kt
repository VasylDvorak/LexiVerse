package com.diplomproject.learningtogether.domain.interactor

import com.diplomproject.learningtogether.domain.entities.LessonIdEntity


interface FavoriteInteractor {
    fun onLikeChange(lessonIdEntity: LessonIdEntity, callback: (Boolean) -> Unit)
    fun changeLike(lessonIdEntity: LessonIdEntity)
}