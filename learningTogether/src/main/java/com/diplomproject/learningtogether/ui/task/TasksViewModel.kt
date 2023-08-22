package com.diplomproject.learningtogether.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.entities.TaskEntity
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.diplomproject.learningtogether.utils.SingleLiveEvent

class TaskViewModel(
    private val coursesRepo: CoursesRepo,
    private val courseId: Long,
    private val lessonId: Long,
    private val favoriteInteractor: FavoriteInteractor,
    private val answerCounterInteractor: AnswerCounterInteractor
) : ViewModel() {

    var currentValueIndex = 0 // Индекс текущего значения
    var negativeTasksIndex = 0
    var tasksValue = 0

    //одно из решений над Mutable (это стандартно принятый этот метод)
    private val _inProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    // сразу когда чтото будет кластся в inProgressLiveData, сразу все подписчики будут получать изменения
    val inProgressLiveData: LiveData<Boolean> = _inProgressLiveData

    val tasksLiveData: LiveData<TaskEntity> = MutableLiveData()
    var tasks: MutableList<TaskEntity> = mutableListOf()

    val selectedSuccessLiveData: LiveData<Unit> = SingleLiveEvent()
    val wrongAnswerLiveData: LiveData<Unit> = SingleLiveEvent()

    //изменение лайка
    val isFavoriteLiveData: LiveData<Boolean> = MutableLiveData()

    init {
        currentValueIndex = 0
        negativeTasksIndex = 0
        tasksValue = 0

        if (tasksLiveData.value == null) {
            _inProgressLiveData.postValue(true)
            coursesRepo.getLesson(courseId, lessonId) {
                it?.let {
                    inProgressLiveData.mutable().postValue(false)
                    tasks = it.tasks//сохранили список на старте запуска
                    tasksValue = it.tasks.size
                    tasksLiveData.mutable().postValue(getNextTask())
                }
            }
        }

        //подписка на старте (изменение лайков)
        favoriteInteractor.onLikeChange(LessonIdEntity(courseId, lessonId)) {
            isFavoriteLiveData.mutable().postValue(it)
        }
    }

    fun onAnswerSelect(userAnswer: String) {
        val currentIndex = currentValueIndex
        val negativeIndex = negativeTasksIndex
        val taskEntity = getNextTask()

        if (checkingAnswer(userAnswer, tasksLiveData.value!!.rightAnswer)) {

            currentValueIndex = currentIndex + 1

//            answerCounterInteractor.getRightCounter() // todo количество положительных ответов

            if (taskEntity == null) {
                selectedSuccessLiveData.mutable().postValue(Unit)
            } else {
                tasksLiveData.mutable().postValue(taskEntity)
            }

            answerCounterInteractor.logRightAnswer()

        } else {
            answerCounterInteractor.logErrorAnswer()
            negativeTasksIndex = negativeIndex + 1
            wrongAnswerLiveData.mutable().postValue(Unit)

            if (taskEntity == null) {
                selectedSuccessLiveData.mutable().postValue(Unit)
            } else {
                tasksLiveData.mutable().postValue(taskEntity)
            }
//            tasksLiveData.mutable().postValue(getNextTask())
        }
    }

    private fun checkingAnswer(userAnswer: String, rightAnswer: String): Boolean {

        return rightAnswer == userAnswer
    }

    private fun getNextTask(): TaskEntity? {
        val nextTask = tasks.randomOrNull()// означает, что рандом может принять null
        tasks.remove(nextTask)//удаляем из списка одно задание
        return nextTask
    }

    //экстеншен (расширение обычной чужай функции). Можно указать mutable расширение и оно вернет версию MutableLiveData
    //это сделано чтобы случайно во фрагменте случайно не изменить список (в этом рельной безописности нет)
    private fun <T> LiveData<T>.mutable(): MutableLiveData<T> {
        return this as MutableLiveData
    }

    fun onLikeClick() {
        favoriteInteractor.changeLike(LessonIdEntity(courseId, lessonId))
    }
}