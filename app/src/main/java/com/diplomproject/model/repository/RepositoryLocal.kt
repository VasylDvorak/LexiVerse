package com.diplomproject.model.repository

import com.diplomproject.model.datasource.AppState
import com.diplomproject.model.data_word_request.DataModel


interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
    suspend fun putFavorite(favorite: DataModel)
    suspend fun removeFavoriteItem(favorite: DataModel)

}
