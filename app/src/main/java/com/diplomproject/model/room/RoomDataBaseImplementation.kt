package com.diplomproject.model.room

import com.diplomproject.model.data_description_request.DataModelId
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.repository.RepositoryDataSourceLocal
import com.diplomproject.model.room.favorite.FavoriteDao
import com.diplomproject.utils.converterDataModelToFavoriteEntity
import com.diplomproject.utils.mapFavoriteEntityToSearchResult

class RoomDataBaseImplementation(
    private val favoriteDao: FavoriteDao
) : RepositoryDataSourceLocal<List<DataModel>> {


    override suspend fun getDataId(id: String): List<DataModelId> = listOf()


    override suspend fun putFavorite(favorite: DataModel) {
        favoriteDao.insert(converterDataModelToFavoriteEntity(favorite))
    }

    override suspend fun getFavoriteList(): List<DataModel> {
        return mapFavoriteEntityToSearchResult(favoriteDao.all())
    }

    override suspend fun removeFavoriteItem(favorite: DataModel) {
        favorite.text?.let { favoriteDao.deleteItem(it) }
    }
}
