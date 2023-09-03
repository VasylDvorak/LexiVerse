package com.diplomproject.view.main_fragment

import android.content.Context
import androidx.lifecycle.LiveData
import com.diplomproject.R
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.utils.parseSearchResults
import com.diplomproject.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

private const val QUERY = "query"

class MainViewModel(var interactor: MainInteractor) : BaseViewModel<AppState>() {

    private val contextApp by lazy { KoinPlatform.getKoin().get<Context>() }
    private val liveDataForViewToObserve: LiveData<AppState> = _liveDataForViewToObserve

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
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
        super.onCleared()
    }


    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {

        queryStateFlow.value = Pair(word, isOnline)
        coroutineScope.launch {
            queryStateFlow.filter { query ->
                if (query.first.isEmpty()) {
                    _liveDataForViewToObserve.postValue(AppState.Error(Throwable(
                        contextApp.getString(R.string.empty_string_message))))
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
                            emit(AppState.Error(Throwable(contextApp
                                .getString(R.string.error_message))))
                        }
                }
                .filterNotNull()
                .collect { result ->
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

    fun removeFromFavorite(dataModel: DataModel) {
        coroutineScope.launch {
            interactor.removeFavoriteItem(
                dataModel
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
    fun playContentUrl(url: String) {
        interactor.playContentUrl(url)
    }

    fun releaseMediaPlayer() {
        interactor.releaseMediaPlayer()
    }
}