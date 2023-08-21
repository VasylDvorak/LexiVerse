package com.diplomproject.model.repository

import com.diplomproject.model.data_word_request.Meanings
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_word_request.DataModel


interface Repository<T> {

    suspend fun getData(word: String): T
    suspend fun getFavoriteList(): T
   suspend fun getDataDescription(meanings: List<Meanings>): List<Example> = listOf()
    suspend fun getDataTestEnglish(dataModels: List<DataModel>): List<Example> = listOf()
}
