package com.diplomproject.learningtogether.ui.learning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.entities.TaskEntity
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.diplomproject.learningtogether.utils.SingleLiveEvent

class LearningViewModel(
    private val coursesRepo: CoursesRepo,
    private val courseId: Long,
    private val lessonId: Long,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val _inProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val currentValueIndex = MutableLiveData<Int>() // Индекс текущего значения
    val isLastValue =
        MutableLiveData<Boolean>() // Флаг, указывающий, достигнуто ли последнее значение

    // сразу когда чтото будет кластся в inProgressLiveData, сразу все подписчики будут получать изменения
    val inProgressLiveData: LiveData<Boolean> = _inProgressLiveData

    val learningLiveData: LiveData<TaskEntity> = MutableLiveData()
    var learningList: MutableList<TaskEntity> = mutableListOf()


    val selectedSuccessLiveData: LiveData<Unit> = SingleLiveEvent()
    val wrongAnswerLiveData: LiveData<Unit> = SingleLiveEvent()

    //изменение лайка
    val isFavoriteLiveData: LiveData<Boolean> = MutableLiveData()

    init {
        if (learningLiveData.value == null) {
            _inProgressLiveData.postValue(true)
            coursesRepo.getLesson(courseId, lessonId) {
                it?.let {
                    inProgressLiveData.mutable().postValue(false)
                    learningList = it.tasks//сохранили список на старте запуска
                    learningLiveData.mutable().postValue(getNextTask())
                }
            }
        }

        //подписка на старте (изменение лайков)
        favoriteInteractor.onLikeChange(LessonIdEntity(courseId, lessonId)) {
            isFavoriteLiveData.mutable().postValue(it)
        }
    }

    fun onAnswerSelect(userAnswer: String) {
        if (checkingAnswer(userAnswer, learningLiveData.value!!.rightAnswer)) {
            val taskEntity = getNextTask()
            if (taskEntity == null) {
                selectedSuccessLiveData.mutable().postValue(Unit)
            } else {
                learningLiveData.mutable().postValue(taskEntity)
            }
        } else {
            wrongAnswerLiveData.mutable().postValue(Unit)
        }
    }

    private fun checkingAnswer(userAnswer: String, rightAnswer: String): Boolean {
        return rightAnswer == userAnswer
    }

    private fun getNextTask(): TaskEntity? {
        val nextTask = learningList.randomOrNull()// означает, что рандом может принять null
        learningList.remove(nextTask)//удаляем из списка одно задание
        return nextTask
    }

    private fun <T> LiveData<T>.mutable(): MutableLiveData<T> {
        return this as MutableLiveData
    }

    fun onLikeClick() {
        favoriteInteractor.changeLike(LessonIdEntity(courseId, lessonId))
    }


    // Метод для инициализации значений
    fun initValues() {
        currentValueIndex.value = 0
        isLastValue.value = false
    }

    // Метод для переключения на предыдущее значение
    fun navigateToPreviousValue() {
        val currentIndex = currentValueIndex.value ?: 0
        if (currentIndex > 0) {
            currentValueIndex.value = currentIndex - 1
        }
    }

    // Метод для переключения на следующее значение
    fun navigateToNextValue() {
        val currentIndex = currentValueIndex.value ?: 0
        if (currentIndex < learningList.size - 1) {
            currentValueIndex.value = currentIndex + 1
        } else {
            isLastValue.value = true
        }
    }
}