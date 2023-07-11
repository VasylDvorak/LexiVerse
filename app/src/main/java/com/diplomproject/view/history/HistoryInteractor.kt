package com.diplomproject.view.history

import com.diplomproject.model.data.AppState
import com.diplomproject.model.data.DataModel
import com.diplomproject.model.repository.RepositoryLocal
import com.diplomproject.viewmodel.Interactor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class HistoryInteractor(
    val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState> {
        return MutableStateFlow(
            AppState.Success(
                repositoryLocal.getData(word)
            )
        )
    }

    override suspend fun getWordFromDB(word: String): Flow<DataModel> {
        return MutableStateFlow(
            repositoryLocal.findWordInDB(word)
        )
    }

    override suspend fun putFavorite(favorite: DataModel) {
        repositoryLocal.putFavorite(favorite)
    }


}
