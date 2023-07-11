package com.diplomproject.view.favorite

import com.diplomproject.model.data.AppState
import com.diplomproject.model.data.DataModel
import com.diplomproject.model.repository.RepositoryLocal
import com.diplomproject.viewmodel.Interactor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FavoriteInteractor(
    val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): Flow<AppState> {
        return MutableStateFlow(
            AppState.Success(
                repositoryLocal.getFavoriteList()
            )
        )
    }

    override suspend fun removeFavoriteItem(removeFavorite: DataModel) {
        repositoryLocal.removeFavoriteItem(removeFavorite)
    }

}
