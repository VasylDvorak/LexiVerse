package com.diplomproject.view.description

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.diplomproject.R
import com.diplomproject.domain.interactors.DescriptionInteractor
import com.diplomproject.model.data_description_request.DescriptionAppState
import com.diplomproject.model.data_word_request.Meanings
import com.diplomproject.utils.parseSearchResultsDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

private const val QUERY_DESCRIPTION = "query_description"

class DescriptionViewModel(var interactor: DescriptionInteractor) : ViewModel() {

    private val contextApp by lazy { KoinPlatform.getKoin().get<Context>() }
    var _liveDataForDescriptionViewToObserve: MutableLiveData<DescriptionAppState> =
        MutableLiveData()
    private val liveDataForViewToObserve: LiveData<DescriptionAppState> =
        _liveDataForDescriptionViewToObserve
    var savedStateHandle: SavedStateHandle = SavedStateHandle()
    protected var queryStateFlow = MutableStateFlow(Pair(listOf<Meanings>(), true))
    var coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun subscribe(): LiveData<DescriptionAppState> {
        return liveDataForViewToObserve
    }

    fun setQuery(query: DescriptionAppState) {
        savedStateHandle[QUERY_DESCRIPTION] = query
    }

    public override fun onCleared() {
        _liveDataForDescriptionViewToObserve.value = DescriptionAppState.Success(null)
        super.onCleared()
        cancelJob()
    }


    fun getDataDescription(
        meanings: List<Meanings>,
        isOnline: Boolean
    ): LiveData<DescriptionAppState> {

        queryStateFlow.value = Pair(meanings, isOnline)
        coroutineScope.launch {
            queryStateFlow.filter { query ->
                if (query.first.isEmpty()) {
                    _liveDataForDescriptionViewToObserve.postValue(
                        DescriptionAppState.Error(
                            Throwable(contextApp.getString(R.string.empty_string_message))
                        )
                    )
                    return@filter false
                } else {
                    return@filter true
                }
            }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emit(
                                DescriptionAppState.Error(
                                    Throwable(
                                        contextApp
                                            .getString(R.string.error_message)
                                    )
                                )
                            )
                        }
                }
                .filterNotNull()
                .collect { result ->
                    result
                    _liveDataForDescriptionViewToObserve.postValue(result)
                }
        }

        return _liveDataForDescriptionViewToObserve
    }

    fun dataFromNetwork(query: Pair<List<Meanings>, Boolean>): Flow<DescriptionAppState> {
        return flow {
            emit(
                parseSearchResultsDescription(
                    interactor.getData(
                        query.first,
                        query.second
                    )
                )
            )
        }
    }

    protected fun cancelJob() {
        queryStateFlow = MutableStateFlow(Pair(listOf(), true))
    }

    fun playContentUrl(url: String) {
        interactor.playContentUrl(url)
    }

    fun releaseMediaPlayer() {
        interactor.releaseMediaPlayer()
    }
}