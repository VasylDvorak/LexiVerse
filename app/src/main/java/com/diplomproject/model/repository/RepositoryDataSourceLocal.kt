package com.diplomproject.model.repository

import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState


interface RepositoryDataSourceLocal<T> : RepositoryDataSource<T> {

    suspend fun saveToDB(appState: AppState) {}
    suspend fun putFavorite(favorite: DataModel)
    suspend fun getFavoriteList(): List<DataModel>
    suspend fun removeFavoriteItem(favorite: DataModel)
}
