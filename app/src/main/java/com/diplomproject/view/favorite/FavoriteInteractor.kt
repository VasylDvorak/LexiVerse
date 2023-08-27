package com.diplomproject.view.favorite

import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.model.repository.RepositoryLocal
import com.diplomproject.utils.network.SharedPreferencesDelegate
import com.diplomproject.view.main_fragment.LIST_KEY
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
        saveListInSharedPref(removeFavorite)
        repositoryLocal.removeFavoriteItem(removeFavorite)
    }

    private fun saveListInSharedPref(dataModel: DataModel) {
        var listFromSharedPreferences: List<DataModel> by SharedPreferencesDelegate(LIST_KEY)
        if (!listFromSharedPreferences.isNullOrEmpty()) {
            var saveToSharedPreference = listFromSharedPreferences
            saveToSharedPreference.forEach {
                if (it.text == dataModel.text) {
                    it.inFavoriteList = false
                }
            }
            listFromSharedPreferences = saveToSharedPreference
        }
    }

}
