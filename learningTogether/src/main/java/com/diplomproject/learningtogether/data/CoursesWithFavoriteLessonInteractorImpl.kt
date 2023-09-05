package com.diplomproject.learningtogether.data

import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.domain.interactor.CoursesWithFavoriteLessonInteractor
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.diplomproject.learningtogether.domain.repos.FavoriteLessonsRepo

/**
 * необходимо получить курсы и лайки
 * нужны два репозитория на вход
 */

class CoursesWithFavoriteLessonInteractorImpl(
    private val coursesRepo: CoursesRepo,
    private val favoriteLessonsRepo: FavoriteLessonsRepo
) : CoursesWithFavoriteLessonInteractor {

    override fun getCourses(onSuccess: (MutableList<CourseWithFavoriteLessonEntity>) -> Unit) {
        coursesRepo.getCourses { courseList ->
            val rawCourses =
                courseList.map { it.mapToCourseWithFavoriteLessonEntity() }.toMutableList()
            rawCourses.forEach { course ->
                course.lessons.forEach { lesson ->
                    lesson.isFavorite = favoriteLessonsRepo.isFavorite(
                        course.id,
                        lesson.id
                    )
                }
            }
            onSuccess(rawCourses)
        }
    }

    override fun getCourse(id: Long, onSuccess: (CourseWithFavoriteLessonEntity?) -> Unit) {
        coursesRepo.getCourse(id) {
            val rawCourse = it?.mapToCourseWithFavoriteLessonEntity()
            rawCourse?.lessons?.forEach { lesson ->
                lesson.isFavorite = favoriteLessonsRepo.isFavorite(
                    id,
                    lesson.id
                )
            }
            onSuccess(rawCourse)
        }
    }

    override fun getLesson(
        courseId: Long,
        lessonId: Long,
        onSuccess: (FavoriteLessonEntity?) -> Unit
    ) {
        val isFavorite =
            favoriteLessonsRepo.isFavorite(courseId, lessonId)
        coursesRepo.getLesson(courseId, lessonId) {
            onSuccess(it?.mapToFavoriteLesson(isFavorite))
        }
    }
}