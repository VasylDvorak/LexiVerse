package com.diplomproject.learningtogether.data

import com.diplomproject.learningtogether.domain.entities.CourseEntity
import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.LessonEntity

/**
 * Расширения (extensions). Копируем один объект в другой.
 * Мы здесь скопировали сущьность (преобразовали) и добавили (модифицировали)
 * старую сущьность новыми полями.
 * extensions - позволяет расширять класс путём добавления нового функционала без
 * необходимости наследования от такого класса и использования паттернов. Это реализовано с помощью
 * специальных выражений, называемых расширения.
 */

fun LessonEntity.mapToFavoriteLesson(
    isFavorite: Boolean = false,
    courseId: Long
): FavoriteLessonEntity {
    return FavoriteLessonEntity(
        id = this.id,
        name = name,
        imageUrl = imageUrl,
        victoryImageUrl = victoryImageUrl,
        tasks = ArrayList(tasks),//сделали копию листа
        isFavorite = isFavorite,
        courseId = courseId
    )
}

fun CourseEntity.mapToCourseWithFavoriteLessonEntity(): CourseWithFavoriteLessonEntity {
    return CourseWithFavoriteLessonEntity(
        id = id,
        name = name,
        logoUrl = logoUrl,
        //у lessons другой тип, поэтому делаем преобразование к соответствующему типу
        lessons = lessons.map { it.mapToFavoriteLesson(courseId = id) }.toMutableList()
        //берем список, для каждого члена списка применяем преобразование -> it.mapToFavoriteLesson(),
        // долее превращаем в общий список -> toMutableList()
        // Очень важный момент - не передаем факт избранности
    )
}