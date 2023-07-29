package com.diplomproject.model.datasource


import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.data_description_request.DataModelId
import org.koin.java.KoinJavaComponent.getKoin


class RetrofitImplementation : DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        val getService = getKoin().get<ApiService>()
        return getService.searchAsync(word).await()
    }

    override suspend fun getDataId(id: String): List<DataModelId> {
        val getService = getKoin().get<ApiService>()
        return getService.searchIdAsync(id).await()
    }

}
