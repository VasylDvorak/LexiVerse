package com.diplomproject.utils

import com.diplomproject.model.data.AppState
import com.diplomproject.model.data.DataModel
import com.diplomproject.model.data.Meanings
import com.diplomproject.model.data.Translation
import com.diplomproject.room.favorite.FavoriteEntity
import com.diplomproject.room.history.HistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


suspend fun parseSearchResults(state: Flow<AppState>): AppState {
    var newSearchResults = ArrayList<DataModel>()
    var appState = state.first()

    when (appState) {
        is AppState.Success -> {
            var searchResults = appState.data!!

            if (!searchResults.isNullOrEmpty()) {
                for (searchResult in searchResults) {
                    parseResult(searchResult, newSearchResults)
                }
            }
            appState = AppState.Success(searchResults)
        }

        else -> {}
    }
    return appState
}


suspend fun parseLocalSearchResults(appState: Flow<AppState>): AppState {
    return AppState.Success(mapResult(appState.first(), false))
}

suspend fun parseWordSearchResults(findWordFlow: Flow<DataModel>): DataModel {
    return findWordFlow.first()
}

private fun mapResult(
    appState: AppState,
    isOnline: Boolean
): List<DataModel> {
    val newSearchResults = arrayListOf<DataModel>()
    when (appState) {
        is AppState.Success -> {
            getSuccessResultData(appState, isOnline, newSearchResults)
        }

        else -> {
            null
        }
    }
    return newSearchResults
}


private fun getSuccessResultData(
    appState: AppState.Success,
    isOnline: Boolean,
    newDataModels: ArrayList<DataModel>
) {
    val dataModels: List<DataModel> = appState.data as List<DataModel>
    if (dataModels.isNotEmpty()) {
        if (isOnline) {
            for (searchResult in dataModels) {
                parseOnlineResult(searchResult, newDataModels)
            }
        } else {
            for (searchResult in dataModels) {

                newDataModels.add(
                    DataModel(
                        searchResult.text,
                        listOf(searchResult.meanings?.get(0) ?: Meanings())
                    )
                )
            }
        }
    }
}

private fun parseOnlineResult(
    dataModel: DataModel, newDataModels:
    ArrayList<DataModel>
) {
    if (!dataModel.text.isNullOrBlank() && !dataModel.meanings.isNullOrEmpty()) {
        val newMeanings = arrayListOf<Meanings>()
        for (meaning in dataModel.meanings) {
            if (meaning.translation != null &&
                !meaning.translation.translation.isNullOrBlank()
            ) {
                newMeanings.add(
                    Meanings(
                        meaning.translation,
                        meaning.imageUrl,
                        meaning.transcription,
                        meaning.soundUrl
                    )
                )
            }
        }
        if (newMeanings.isNotEmpty()) {
            newDataModels.add(DataModel(dataModel.text, newMeanings))
        }
    }
}


fun parseResult(dataModel: DataModel, newDataModels: ArrayList<DataModel>): ArrayList<DataModel> {
    if (!dataModel.text.isNullOrBlank() && !dataModel.meanings.isNullOrEmpty()) {
        val newMeanings = arrayListOf<Meanings>()
        for (meaning in dataModel.meanings) {
            if (meaning.translation != null && !meaning.translation.translation.isNullOrBlank()) {
                newMeanings.add(
                    Meanings(
                        meaning.translation,
                        meaning.imageUrl, meaning.transcription, meaning.soundUrl
                    )
                )
            }
        }


        if (newMeanings.isNotEmpty()) {
            newDataModels.add(DataModel(dataModel.text, newMeanings))
        }
    }
    return newDataModels
}

fun mapHistoryEntityToSearchResult(list: List<HistoryEntity>): List<DataModel> {
    val searchResult = ArrayList<DataModel>()
    if (!list.isNullOrEmpty()) {
        for (entity in list) {
            val meanings = Meanings(
                Translation(entity.translation),
                entity.imageUrl, entity.transcription, entity.soundUrl
            )

            searchResult.add(DataModel(entity.word, listOf(meanings)))
        }
    }
    return searchResult
}

fun mapFavoriteEntityToSearchResult(list: List<FavoriteEntity>): List<DataModel> {
    val searchResult = ArrayList<DataModel>()
    if (!list.isNullOrEmpty()) {
        for (entity in list) {
            val meanings = Meanings(
                Translation(entity.translation),
                entity.imageUrl, entity.transcription, entity.soundUrl
            )

            searchResult.add(DataModel(entity.word, listOf(meanings)))
        }
    }
    return searchResult
}

fun convertDataModelSuccessToEntity(appState: AppState): HistoryEntity? {
    return when (appState) {
        is AppState.Success -> {
            val searchResult = appState.data
            if (searchResult.isNullOrEmpty() || searchResult[0].text.isNullOrEmpty()) {
                null
            } else {
                var meanings = searchResult[0].meanings?.get(0)
                HistoryEntity(
                    searchResult[0].text!!, meanings?.imageUrl,
                    meanings?.transcription, meanings?.soundUrl, meanings?.translation!!.translation
                )
            }
        }

        else -> null
    }
}

fun converterDataModelToFavoriteEntity(sourse: DataModel): FavoriteEntity {
    var meanings = sourse.meanings?.get(0)
    return FavoriteEntity(
        sourse.text!!, meanings?.imageUrl,
        meanings?.transcription, meanings?.soundUrl, meanings?.translation!!.translation
    )

}



