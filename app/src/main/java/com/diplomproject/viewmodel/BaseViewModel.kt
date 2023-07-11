package com.diplomproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.diplomproject.model.data.AppState
import com.diplomproject.model.data.DataModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<T : AppState>(
    open var _liveDataForViewToObserve: MutableLiveData<T> = MutableLiveData(),
    protected open var _liveDataFindWordInHistory: MutableLiveData<DataModel> = MutableLiveData(),
    var savedStateHandle: SavedStateHandle = SavedStateHandle(),
    protected open val _mutableLiveData: MutableLiveData<T> = MutableLiveData()
) : ViewModel() {

    protected var queryStateFlow = MutableStateFlow(Pair("", true))
    protected var queryStateFlowFindWordFromHistory = MutableStateFlow("")
    protected var job: Job = Job()
    var coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    protected fun cancelJob() {
        queryStateFlow = MutableStateFlow(Pair("", true))
    }

    open fun getData(word: String, isOnline: Boolean): LiveData<T> =
        _liveDataForViewToObserve

    open fun findWordInHistory(word: String): LiveData<DataModel> =
        _liveDataFindWordInHistory

    abstract fun handleError(error: Throwable)


    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })
}
