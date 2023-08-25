package com.diplomproject.model.datasource

import com.diplomproject.model.data_description_request.DataModelId
import com.diplomproject.model.data_word_request.DataModel


interface DataSource<T> {

    suspend fun getDataId(id: String): List<DataModelId>

    suspend fun getData(word: String): List<DataModel> = listOf()
}
