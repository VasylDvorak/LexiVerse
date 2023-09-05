package com.diplomproject.learningtogether.data

import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo


class FavoriteInteractionImpl(
    private val favoriteRepo: FavoriteLessonsRepo
) : FavoriteInteractor {

    private val listeners: MutableList<Pair<LessonIdEntity, (Boolean) -> Unit>> = mutableListOf()

    override fun onLikeChange(lessonIdEntity: LessonIdEntity, callback: (Boolean) -> Unit) {
        val isFavorite = favoriteRepo.isFavorite(lessonIdEntity.courseId, lessonIdEntity.lessonId)
        callback(isFavorite)

        listeners.add(Pair(lessonIdEntity, callback))
    }

    override fun changeLike(lessonIdEntity: LessonIdEntity) {
        val isFavorite = favoriteRepo.isFavorite(lessonIdEntity.courseId, lessonIdEntity.lessonId)
        if (isFavorite) {
            favoriteRepo.removeEntity(lessonIdEntity)
        } else {
            favoriteRepo.addFavorite(lessonIdEntity)
        }
        notifyListeners(lessonIdEntity, !isFavorite)
    }

    private fun notifyListeners(lessonIdEntity: LessonIdEntity, isFavorite: Boolean) {
        listeners.forEach {
            if (it.first == lessonIdEntity) {
                it.second.invoke(isFavorite)
            }
        }
    }
}