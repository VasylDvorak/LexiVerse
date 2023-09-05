package com.diplomproject.learningtogether.ui.lessons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.domain.interactor.CoursesWithFavoriteLessonInteractor
import com.diplomproject.learningtogether.utils.SingleLiveEvent

class LessonsViewModel(
    private val coursesInteractor: CoursesWithFavoriteLessonInteractor,
    private val lessonId: Long
) : ViewModel() {

    private val _inProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val inProgressLiveData: LiveData<Boolean> = _inProgressLiveData
    val coursesLiveData: LiveData<CourseWithFavoriteLessonEntity> = MutableLiveData()
    val selectedLessonsLiveData: LiveData<FavoriteLessonEntity> = SingleLiveEvent()

    init {
        if (coursesLiveData.value == null) {
            _inProgressLiveData.postValue(true)
            coursesInteractor.getCourse(lessonId) {
                it?.let {
                    inProgressLiveData.mutable().postValue(false)
                    coursesLiveData.mutable().postValue(it)
                }
            }
        }
    }

    fun onLessonClick(lessonEntity: FavoriteLessonEntity) {
        (selectedLessonsLiveData as MutableLiveData).value =
            lessonEntity
    }

    private fun <T> LiveData<T>.mutable(): MutableLiveData<T> {
        return this as MutableLiveData
    }
}