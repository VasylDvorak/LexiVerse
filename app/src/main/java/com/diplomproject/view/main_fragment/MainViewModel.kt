package com.diplomproject.view.main_fragment

import androidx.lifecycle.LiveData
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.utils.parseSearchResults
import com.diplomproject.utils.parseWordSearchResults
import com.diplomproject.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val QUERY = "query"

class MainViewModel(var interactor: MainInteractor) : BaseViewModel<AppState>() {

    private val liveDataForViewToObserve: LiveData<AppState> = _liveDataForViewToObserve
    private val liveDataFindWordInHistory: LiveData<DataModel> = _liveDataFindWordInHistory


    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    fun subscribeFindWord(): LiveData<DataModel> {
        return liveDataFindWordInHistory
    }

    fun getRestoredData(): AppState? = savedStateHandle[QUERY]
    fun setQuery(query: AppState) {
        savedStateHandle[QUERY] = query
    }

    // Обрабатываем ошибки
    override fun handleError(error: Throwable) {
        _liveDataForViewToObserve.postValue(AppState.Error(error))
    }


    public override fun onCleared() {
        _liveDataForViewToObserve.value = AppState.Success(null)
        _liveDataFindWordInHistory.value = null
        super.onCleared()
    }

    override fun findWordInHistory(word: String): LiveData<DataModel> {
        queryStateFlowFindWordFromHistory.value = word
        coroutineScope.launch {
            queryStateFlowFindWordFromHistory.filter { query ->
                if (query.isEmpty() || (query == "")) {
                    _liveDataFindWordInHistory.postValue(DataModel())
                    return@filter false
                } else {
                    return@filter true
                }
            }
                // .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    findWordFromHistory(query)
                        .catch {
                            emit(DataModel())
                        }
                }
                .filterNotNull()
                .collect { result ->
                    _liveDataFindWordInHistory.postValue(result)
                }
        }
        return super.findWordInHistory(word)
    }


    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {

        queryStateFlow.value = Pair(word, isOnline)
        coroutineScope.launch {
            queryStateFlow.filter { query ->
                if (query.first.isEmpty()) {
                    _liveDataForViewToObserve.postValue(AppState.Error(Throwable("Пустая строка")))
                    return@filter false
                } else {
                    return@filter true
                }
            }
                // .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emit(AppState.Error(Throwable("Ошибка")))
                        }
                }
                .filterNotNull()
                .collect { result ->
                    result
                    _liveDataForViewToObserve.postValue(result)
                }
        }

        return super.getData(word, isOnline)
    }

    fun dataFromNetwork(query: Pair<String, Boolean>): Flow<AppState> {
        return flow {
            emit(
                parseSearchResults(
                    interactor.getData(
                        query.first,
                        query.second
                    )
                )
            )
        }
    }


    private fun findWordFromHistory(query: String): Flow<DataModel> {
        return flow {
            emit(
                parseWordSearchResults(
                    interactor.getWordFromDB(
                        query
                    )
                )
            )
        }
    }

    fun putInFavorite(favorite: DataModel) {
        coroutineScope.launch {
            interactor.putFavorite(
                favorite
            )
        }
    }

}