package com.diplomproject.view.favorite

import android.content.Context
import android.media.MediaPlayer
import android.preference.PreferenceManager
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.datasource.AppState
import com.diplomproject.model.repository.RepositoryLocal
import com.diplomproject.utils.network.SharedPreferencesDelegate
import com.diplomproject.view.main_fragment.LIST_KEY
import com.diplomproject.view.notification.REFERENCE
import com.diplomproject.viewmodel.Interactor
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.mp.KoinPlatform.getKoin

const val NOTIFICATION_SETTINGS = "NOTIFICATION_SETTINGS"
class FavoriteInteractor(
    val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override var mMediaPlayer: MediaPlayer? = null
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

    fun saveInFireBaseDatabase(listDataModels: List<DataModel>){
        val database = Firebase.database
        val myRef = database.getReference(REFERENCE)
        myRef.setValue(listDataModels)
    }

    fun setSettingsForNotificator(pair: Pair<Boolean, Int>){
        val appSharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(getKoin().get<Context>()
                .applicationContext)
        val prefsEditor = appSharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(pair)
        prefsEditor.putString(NOTIFICATION_SETTINGS, json)
        prefsEditor.apply()
    }
}
