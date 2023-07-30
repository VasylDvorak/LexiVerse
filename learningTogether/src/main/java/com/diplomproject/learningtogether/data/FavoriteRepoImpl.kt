package com.diplomproject.learningtogether.data

import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo


/**
 * Set - это такая сущьность котороя позволяет включать в себя один элемент.
 * То-есть, если он уже есть, второй раз он не добавится.
 */

class FavoriteRepoImpl : FavoriteLessonsRepo {

    private val data: MutableSet<LessonIdEntity> = HashSet()//массив сущьностей

    init {
        data.add(LessonIdEntity(1, 10))
        data.add(LessonIdEntity(2, 501))
        data.add(LessonIdEntity(3, 77))
        data.add(LessonIdEntity(1, 30))
    }

    override fun addFavorite(lessonId: LessonIdEntity) {
        data.add(lessonId)//добавили
    }

    override fun removeEntity(lessonId: LessonIdEntity) {
        data.remove(lessonId)//удалили
    }

    override fun getFavorites(): List<LessonIdEntity> {
        return ArrayList(data)//получили
    }

    override fun isFavorite(courseId: Long, lessonId: Long): Boolean {
        val lessonId = LessonIdEntity(courseId, lessonId)
        return data.contains(lessonId)// проверяем элемент (если такой элемемент содержится, возвращаем true)
    }
}