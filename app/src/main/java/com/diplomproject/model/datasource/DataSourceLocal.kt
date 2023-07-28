package com.diplomproject.model.datasource

import com.diplomproject.model.data_word_request.AppState
import com.diplomproject.model.data_word_request.DataModel


interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)
    suspend fun findWordFromDB(word: String): DataModel
    suspend fun putFavorite(favorite: DataModel)
    suspend fun getFavoriteList(): List<DataModel>
    suspend fun removeFavoriteItem(favorite: DataModel)
}
