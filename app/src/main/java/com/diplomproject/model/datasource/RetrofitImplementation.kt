package com.diplomproject.model.datasource


import com.diplomproject.model.data.DataModel
import org.koin.java.KoinJavaComponent.getKoin


class RetrofitImplementation : DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        val getService = getKoin().get<ApiService>()
        return getService.searchAsync(word).await()
    }
}
