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

    var currentValueIndex = 0
    val needShowFinishScreen =
        SingleLiveEvent<Boolean>()

    val needShowBackButton: LiveData<Int> =
        MutableLiveData()

    val inProgressLiveData: LiveData<Boolean> = _inProgressLiveData

    val learningLiveData: LiveData<TaskEntity> = MutableLiveData()
    var learningList: MutableList<TaskEntity> = mutableListOf()

    val isFavoriteLiveData: LiveData<Boolean> = MutableLiveData()

    init {
        if (learningLiveData.value == null) {
            inProgressLiveData.mutable().postValue(true)
            coursesRepo.getLesson(courseId, lessonId) {
                it?.let {
                    inProgressLiveData.mutable().postValue(false)
                    learningList = it.tasks

                    postTaskByIndex(it.tasks.lastIndex)
                }
            }
        }

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

    fun initValues() {
        currentValueIndex = 0
        needShowFinishScreen.value = false
        visibilityBackButton(currentValueIndex)
    }

    fun navigateToPreviousValue() {
        val currentIndex = currentValueIndex

        if (currentIndex > 0) {

            visibilityBackButton(currentIndex)

            currentValueIndex = currentIndex - 1

            postTaskByIndex(currentIndex)
        }
    }

    fun navigateToNextValue() {
        val currentIndex = currentValueIndex

        if (currentIndex < learningList.size - 1) {

            visibilityBackButton(currentIndex)

            currentValueIndex = currentIndex + 1

            postTaskByIndex(currentIndex)
        } else {
            needShowFinishScreen.value = true
        }
    }

    private fun visibilityBackButton(index: Int) {
        if (index == 0) {
            needShowBackButton.mutable().postValue(0)
        } else {
            needShowBackButton.mutable().postValue(1)
        }
    }

    private fun postTaskByIndex(index: Int) {
        val task = learningList[index]

        meaningRepo.getImageUrl(task.task) {
            task.taskImageUrl = it
            learningLiveData.mutable().postValue(task)
        }
    }
}