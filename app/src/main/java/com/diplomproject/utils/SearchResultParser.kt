package com.diplomproject.utils

import com.diplomproject.model.data_description_request.DescriptionAppState
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.data_word_request.Meanings
import com.diplomproject.model.data_word_request.Translation
import com.diplomproject.model.datasource.AppState
import com.diplomproject.model.room.favorite.FavoriteEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

private val gson by lazy { Gson() }

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

suspend fun parseSearchResultsDescription(state: Flow<DescriptionAppState>): DescriptionAppState {
    var descriptionAppState = state.first()


    when (descriptionAppState) {
        is DescriptionAppState.Success -> {
            var searchResults = descriptionAppState.data!!
            descriptionAppState = DescriptionAppState.Success(searchResults)
        }

        else -> {}
    }
    return descriptionAppState
}


suspend fun parseLocalSearchResults(appState: Flow<AppState>): AppState {
    return AppState.Success(mapResult(appState.first(), false))
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
                        searchResult.id,
                        searchResult.text,
                        listOf(searchResult.meanings?.get(0) ?: Meanings()),
                        searchResult.exampleDataModel
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
                        meaning.id,
                        meaning.partOfSpeechCode,
                        meaning.translation,
                        meaning.imageUrl,
                        meaning.transcription,
                        meaning.soundUrl
                    )
                )
            }
        }
        if (newMeanings.isNotEmpty()) {
            newDataModels.add(DataModel(dataModel.id, dataModel.text, newMeanings))
        }
    }
}


fun parseResult(dataModel: DataModel, newDataModels: ArrayList<DataModel>): ArrayList<DataModel> {
    if (dataModel.text.isNullOrBlank() || dataModel.meanings.isNullOrEmpty()) {
        return newDataModels
    }
    val newMeanings = arrayListOf<Meanings>()
    for (meaning in dataModel.meanings) {
        if (meaning.translation != null && !meaning.translation.translation.isNullOrBlank()) {
            newMeanings.add(
                Meanings(
                    meaning.id,
                    meaning.partOfSpeechCode,
                    meaning.translation,
                    meaning.imageUrl,
                    meaning.transcription,
                    meaning.soundUrl
                )
            )
        }
    }


    if (newMeanings.isNotEmpty()) {
        newDataModels.add(DataModel(dataModel.id, dataModel.text, newMeanings))
    }
    return newDataModels
}


fun mapFavoriteEntityToSearchResult(list: List<FavoriteEntity>): List<DataModel> {
    val searchResult = ArrayList<DataModel>()
    if (list.isNullOrEmpty()) {
        return searchResult
    }
    for (entity in list) {
        val meanings = Meanings(
            entity.id,
            entity.partOfSpeechCode,
            Translation(entity.translation),
            entity.imageUrl,
            entity.transcription,
            entity.soundUrl
        )
        val type = object : TypeToken<List<Example?>?>() {}.type
        val exampleList = Gson().fromJson(entity.examples, type) as List<Example>
        searchResult.add(DataModel(entity.id, entity.word, listOf(meanings), exampleList))
    }
    return searchResult
}


fun converterDataModelToFavoriteEntity(sourse: DataModel): FavoriteEntity {
    var meanings = sourse.meanings?.get(0)
    return FavoriteEntity(
        meanings?.id,
        sourse.text!!,
        meanings?.imageUrl,
        meanings?.transcription,
        meanings?.soundUrl,
        meanings?.translation!!.translation,
        gson.toJson(sourse.exampleDataModel),
        meanings.partOfSpeechCode
    )

}
