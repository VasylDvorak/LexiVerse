package com.diplomproject.learningtogether.data

import android.content.Context
import com.diplomproject.learningtogether.Key.ASSETS_LESSONS_TASK_KEY
import com.diplomproject.learningtogether.domain.entities.CourseEntity
import com.diplomproject.learningtogether.domain.entities.LessonEntity
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AssetsCoursesRepoImpl(
    private val context: Context
) : CoursesRepo {

    override fun getCourses(onSuccess: (MutableList<CourseEntity>) -> Unit) {

        val databaseJson: String = context.assets.open(ASSETS_LESSONS_TASK_KEY)
            .bufferedReader()
            .use {
                it.readText()
            }

        val courses: MutableList<CourseEntity> =
            Gson()
                .fromJson(databaseJson, object : TypeToken<List<CourseEntity>>() {}
                    .type)
        onSuccess(courses)
    }

    override fun getCourse(id: Long, onSuccess: (CourseEntity?) -> Unit) {
        getCourses {
            val searchResult = it.find { it.id == id }
            onSuccess.invoke(searchResult)
        }
    }

    override fun getLesson(courseId: Long, lessonId: Long, onSuccess: (LessonEntity?) -> Unit) {
        getCourse(courseId) { courseEntity ->
            val searchResult = courseEntity?.lessons?.find { it.id == lessonId }
                ?: throw IllegalStateException("Урока не существует")
            onSuccess.invoke(searchResult)
        }
    }
}
