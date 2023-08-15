package com.diplomproject.view.main_fragment

import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.model.repository.Repository
import com.diplomproject.model.repository.RepositoryLocal
import com.diplomproject.viewmodel.Interactor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainInteractor(
    var repositoryRemote: Repository<List<DataModel>>,
    var repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): StateFlow<AppState> {
        var appState: AppState

        if (fromRemoteSource) {
            appState = AppState.Success(repositoryRemote.getData(word))
            repositoryLocal.saveToDB(appState)
        } else {
            appState = AppState.Success(repositoryLocal.getData(word))
        }
        return MutableStateFlow(appState)
    }

    override suspend fun getWordFromDB(word: String): Flow<DataModel> {
        return MutableStateFlow(
            repositoryLocal.findWordInDB(word)
        )
    }

    override suspend fun putFavorite(favorite: DataModel) {
        repositoryLocal.putFavorite(favorite)
    }

    override suspend fun removeFavoriteItem(removeFavorite: DataModel) {}
}

