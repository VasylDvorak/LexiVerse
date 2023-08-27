package com.diplomproject.view.favorite

import androidx.lifecycle.LiveData
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.utils.parseLocalSearchResults
import com.diplomproject.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteViewModel(var interactor: FavoriteInteractor) :
    BaseViewModel<AppState>() {
    private val liveDataForViewToObserve: LiveData<AppState> = _mutableLiveData
    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {
        _mutableLiveData.value = AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch { startInteractor(word, isOnline) }
        return super.getData(word, isOnline)
    }

    private suspend fun startInteractor(word: String, isOnline: Boolean) {
        _mutableLiveData.postValue(parseLocalSearchResults(interactor.getData(word, isOnline)))
    }


    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }

    public override fun onCleared() {
        _mutableLiveData.value = AppState.Success(null)
        super.onCleared()
    }

    fun remove(dataModel: DataModel) {
        coroutineScope.launch {
            interactor.removeFavoriteItem(
                dataModel
            )
            _mutableLiveData.postValue(
                parseLocalSearchResults(
                    interactor.getData("", false)
                )
            )
        }
    }

}

