package com.diplomproject.learningtogether.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplomproject.learningtogether.domain.entities.CourseWithFavoriteLessonEntity
import com.diplomproject.learningtogether.domain.entities.FavoriteLessonEntity
import com.diplomproject.learningtogether.domain.interactor.CoursesWithFavoriteLessonInteractor
import com.diplomproject.learningtogether.utils.SingleLiveEvent

/**
 * Проблемы MVP
 * 1.Знание презентора о view
 * 2.Хранить и востанавливать состояние (все значеия) в аттач
 * 3.Возвращать все вызовы на главвный поток (нарушать "незнание" проезентора
 * 4*. Не поддерживает гугл и платформой (пишем сами или ищем библиотеки)
 *
 * liveData - это такой тип данных который позволяет подписатся на него и все время получать изменения
 * Тмкже liveData умеет кэшировать значения
 *
 * liveData нельзя создать (это абстрактный класс), но у него есть несколько наследников
 */

class CoursesViewModel(
    private val coursesInteractor: CoursesWithFavoriteLessonInteractor
) : ViewModel() {

    private val _inProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val inProgressLiveData: LiveData<Boolean> = _inProgressLiveData
    val coursesLiveData: LiveData<List<CourseWithFavoriteLessonEntity>> = MutableLiveData()

    val selectedLessonsLiveData: LiveData<Pair<Long, FavoriteLessonEntity>> = SingleLiveEvent()
    val selectedCoursesLiveData: LiveData<CourseWithFavoriteLessonEntity> = SingleLiveEvent()

    init {
        if (coursesLiveData.value == null) {
            _inProgressLiveData.postValue(true)
            coursesInteractor.getCourses {
                inProgressLiveData.mutable().postValue(false)
                coursesLiveData.mutable().postValue(it)
            }
        }
    }

    fun onLessonClick(courseId: Long, lessonEntity: FavoriteLessonEntity) {
        (selectedLessonsLiveData as MutableLiveData).value = Pair(courseId, lessonEntity)
    }

    fun onCourseClick(courseEntity: CourseWithFavoriteLessonEntity) {
        selectedCoursesLiveData.mutable().postValue(courseEntity)
    }

    private fun <T> LiveData<T>.mutable(): MutableLiveData<T> {
        return this as MutableLiveData
    }
}