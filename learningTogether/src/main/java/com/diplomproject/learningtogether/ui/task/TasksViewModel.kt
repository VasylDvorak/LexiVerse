package com.diplomproject.learningtogether.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.entities.TaskEntity
import com.diplomproject.learningtogether.domain.interactor.AnswerCounterInteractor
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.diplomproject.learningtogether.domain.repos.MeaningRepo
import com.diplomproject.learningtogether.utils.SingleLiveEvent

class TaskViewModel(
    private val coursesRepo: CoursesRepo,
    private val courseId: Long,
    private val lessonId: Long,
    private val favoriteInteractor: FavoriteInteractor,
    private val answerCounterInteractor: AnswerCounterInteractor,
    private val meaningRepo: MeaningRepo
) : ViewModel() {

    var currentValueIndex = 0
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
                    tasks = it.tasks
                    tasksValue = it.tasks.size
                    tasksLiveData.mutable().postValue(getNextTask())
//                    postTaskByIndex(getNextTask()!!.task.lastIndex)
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

            if (taskEntity == null) {
                selectedSuccessLiveData.mutable().postValue(Unit)
            } else {
                tasksLiveData.mutable().postValue(taskEntity)
//                postTaskByIndex(taskEntity.level)
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
//                postTaskByIndex(taskEntity.level)
            }
        }

    }

    private fun checkingAnswer(userAnswer: String, rightAnswer: String): Boolean {

        return rightAnswer == userAnswer
    }

    private fun getNextTask(): TaskEntity? {
        val nextTask = tasks.randomOrNull()
        tasks.remove(nextTask)
//        postTaskByIndex(tasks.lastIndex)
        return nextTask
    }

    private fun <T> LiveData<T>.mutable(): MutableLiveData<T> {
        return this as MutableLiveData
    }

    fun onLikeClick() {
        favoriteInteractor.changeLike(LessonIdEntity(courseId, lessonId))
    }

    private fun postTaskByIndex(index: Int) {
        val task = tasks[index]

        meaningRepo.getImageUrl(task.task) {
            task.taskImageUrl = it
            tasksLiveData.mutable().postValue(task)
        }
    }
}