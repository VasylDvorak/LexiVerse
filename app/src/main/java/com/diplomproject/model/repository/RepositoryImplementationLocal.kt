package com.diplomproject.model.repository

import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState


class RepositoryImplementationLocal(private val dataSource: RepositoryDataSourceLocal<List<DataModel>>) :
    RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun getFavoriteList(): List<DataModel> {
        return dataSource.getFavoriteList()
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }

    override suspend fun putFavorite(favorite: DataModel) {
        dataSource.putFavorite(favorite)
    }


    override suspend fun removeFavoriteItem(favorite: DataModel) {
        dataSource.removeFavoriteItem(favorite)
    }

}
