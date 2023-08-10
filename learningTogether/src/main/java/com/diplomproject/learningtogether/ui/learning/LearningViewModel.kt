package com.diplomproject.learningtogether.ui.learning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diplomproject.learningtogether.domain.entities.LessonIdEntity
import com.diplomproject.learningtogether.domain.entities.TaskEntity
import com.diplomproject.learningtogether.domain.interactor.FavoriteInteractor
import com.diplomproject.learningtogether.domain.repos.CoursesRepo
import com.diplomproject.learningtogether.domain.repos.MeaningRepo
import com.diplomproject.learningtogether.utils.SingleLiveEvent

class LearningViewModel(
    private val coursesRepo: CoursesRepo,
    private val courseId: Long,
    private val lessonId: Long,
    private val favoriteInteractor: FavoriteInteractor,
    private val meaningRepo: MeaningRepo
) : ViewModel() {

    private val _inProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var currentValueIndex = 0 // Индекс текущего значения
    val needShowFinishScreen =
        SingleLiveEvent<Boolean>() // Флаг, указывающий, достигнуто ли последнее значение

    // сразу когда чтото будет кластся в inProgressLiveData, сразу все подписчики будут получать изменения
    val inProgressLiveData: LiveData<Boolean> = _inProgressLiveData

    val learningLiveData: LiveData<TaskEntity> = MutableLiveData()
    private var learningList: MutableList<TaskEntity> = mutableListOf()

    //изменение лайка
    val isFavoriteLiveData: LiveData<Boolean> = MutableLiveData()

    init {
        if (learningLiveData.value == null) {
            inProgressLiveData.mutable().postValue(true)
            coursesRepo.getLesson(courseId, lessonId) {
                it?.let {
                    inProgressLiveData.mutable().postValue(false)
                    learningList = it.tasks//сохранили список на старте запуска

//                    postTaskByIndex(it.tasks.lastIndex)

                    learningLiveData.mutable().postValue(learningList[0])
                }
            }
        }

        //подписка на старте (изменение лайков)
        favoriteInteractor.onLikeChange(LessonIdEntity(courseId, lessonId)) {
            isFavoriteLiveData.mutable().postValue(it)
        }
    }

    private fun <T> LiveData<T>.mutable(): MutableLiveData<T> {
        return this as MutableLiveData
    }

    fun onLikeClick() {
        favoriteInteractor.changeLike(LessonIdEntity(courseId, lessonId))
    }


    // Метод для инициализации значений
    fun initValues() {
        currentValueIndex = 0
        needShowFinishScreen.value = false
    }

    // Метод для переключения на предыдущее значение
    fun navigateToPreviousValue() {
        val currentIndex = currentValueIndex
        if (currentIndex > 0) {
            currentValueIndex = currentIndex - 1
//            postTaskByIndex(currentIndex)
            learningLiveData.mutable().postValue(learningList[currentValueIndex])
        }
    }

    // Метод для переключения на следующее значение
    fun navigateToNextValue() {
        val currentIndex = currentValueIndex
        if (currentIndex < learningList.size - 1) {
            currentValueIndex = currentIndex + 1
//            postTaskByIndex(currentIndex)
            learningLiveData.mutable().postValue(learningList[currentValueIndex])

        } else {
            needShowFinishScreen.value = true
        }
    }

    private fun postTaskByIndex(index: Int) {
        val task = learningList[index]

        meaningRepo.getImageUrl(task.task) {
            task.taskImageUrl = it
            learningLiveData.mutable().postValue(task)
        }
    }

//    fun mapTask(taskEntity: TaskEntity): TaskEntity {
//        viewModelScope.launch {
//            val imageUrl = meaningRepo.getImageUrl(taskEntity.task)
//
//            taskEntity.taskImageUrl = imageUrl
//            return taskEntity
//        }
//    }
}