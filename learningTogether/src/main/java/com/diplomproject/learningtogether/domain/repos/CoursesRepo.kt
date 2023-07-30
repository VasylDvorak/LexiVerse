package com.diplomproject.learningtogether.domain.repos

import com.diplomproject.learningtogether.domain.entities.CourseEntity
import com.diplomproject.learningtogether.domain.entities.LessonEntity


interface CoursesRepo {
    fun getCourses(onSuccess: (MutableList<CourseEntity>) -> Unit)
    fun getCourse(id: Long, onSuccess: (CourseEntity?) -> Unit)
    fun getLesson(courseId: Long, lessonId: Long, onSuccess: (LessonEntity?) -> Unit)
}