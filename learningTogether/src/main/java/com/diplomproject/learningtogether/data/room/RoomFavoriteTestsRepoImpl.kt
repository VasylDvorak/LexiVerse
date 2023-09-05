package com.diplomproject.learningtogether.data.room

import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo

class RoomFavoriteTestsRepoImpl(
    private val favoriteLessonDao: FavoriteLessonDao
) : FavoriteLessonsRepo {

    private var data: MutableSet<LessonIdEntity> = HashSet()//массив сущьностей

    override fun addFavorite(lessonId: LessonIdEntity) {
        updateLocalCache()
        favoriteLessonDao.addFavorite(lessonId)
        data.add(lessonId)
    }

    override fun removeEntity(lessonId: LessonIdEntity) {
        updateLocalCache()
        favoriteLessonDao.deleteFavorite(lessonId)
        data.remove(lessonId)
    }

    override fun getFavorites(): List<LessonIdEntity> {
        val newData = favoriteLessonDao.getAllFavorites()
        data = newData.toHashSet()
        return newData
    }

    override fun isFavorite(courseId: Long, lessonId: Long): Boolean {
        updateLocalCache()
        return data.contains(
            LessonIdEntity(
                courseId = courseId,
                lessonId = lessonId
            )
        )
    }

    private fun updateLocalCache() {
        if (data.isEmpty()) {
            getFavorites()
        }
    }
}