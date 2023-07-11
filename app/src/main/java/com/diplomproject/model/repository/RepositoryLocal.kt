package com.diplomproject.model.repository

import com.diplomproject.model.data.AppState
import com.diplomproject.model.data.DataModel


interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
    suspend fun findWordInDB(word: String): DataModel
    suspend fun putFavorite(favorite: DataModel)
    suspend fun removeFavoriteItem(favorite: DataModel)

}
