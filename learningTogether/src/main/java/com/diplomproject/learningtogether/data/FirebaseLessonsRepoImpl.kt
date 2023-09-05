package com.diplomproject.learningtogether.data

import android.os.Handler
import android.os.Looper
import com.diplomproject.learningtogether.Key
import com.diplomproject.learningtogether.domain.entities.CourseEntity
import com.diplomproject.learningtogether.domain.entities.LessonEntity
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseLessonsRepoImpl : CoursesRepo {

    private val database by lazy {
        Firebase.database(Key.DATABASE_URL_KEY).apply { setPersistenceEnabled(true) }
    }

    override fun getCourses(onSuccess: (MutableList<CourseEntity>) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({

            database.reference.keepSynced(true)

            database.reference.get()
                .addOnSuccessListener {
                    val lessons: MutableList<CourseEntity> = mutableListOf()
                    it.children.forEach { snapshot ->
                        try {
                            snapshot.getValue(CourseEntity::class.java)?.let { lesson ->
                                lessons.add(lesson)
                            }
                        } catch (exc: DatabaseException) {
                            exc.printStackTrace()
                        }
                    }
                    onSuccess.invoke(lessons)
                }
        }, 3_000)
    }

    override fun getCourse(id: Long, onSuccess: (CourseEntity?) -> Unit) {
        getCourses { courseEntity ->
            val searchResult = courseEntity.find { it.id == id }
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
