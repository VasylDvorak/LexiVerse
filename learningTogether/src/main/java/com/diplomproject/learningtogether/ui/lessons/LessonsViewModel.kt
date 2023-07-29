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

    //одно из решений над Mutable (это стандартно принятый этот метод)
    private val _inProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    // сразу когда чтото будет кластся в inProgressLiveData, сразу все подписчики будут получать изменения
    val inProgressLiveData: LiveData<Boolean> = _inProgressLiveData
    val coursesLiveData: LiveData<CourseWithFavoriteLessonEntity> = MutableLiveData()
    val selectedLessonsLiveData: LiveData<FavoriteLessonEntity> = SingleLiveEvent()

    init {
        //проверяе на наличие данных в coursesLiveData. Это необходимо для того чтобы при повороте данные не закачивались заново (это костыль)
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
            lessonEntity//Вариант когда агресивно приводим к MutableLiveData
    }

    //экстеншен (расширение обычной чужай функции). Можно указать mutable расширение и оно вернет версию MutableLiveData
    //это сделано чтобы случайно во фрагменте случайно не изменить список (в этом рельной безописности нет)
    private fun <T> LiveData<T>.mutable(): MutableLiveData<T> {
        return this as MutableLiveData
    }
}