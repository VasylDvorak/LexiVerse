package com.diplomproject.model.datasource

import com.diplomproject.model.data_description_request.DataModelId
import com.diplomproject.model.data_word_request.DataModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("words/search")
    fun searchAsync(@Query("search") wordToSearch: String): Deferred<List<DataModel>>

    @GET("meanings")
    fun searchIdAsync(@Query("ids") idToSearch: String): Deferred<List<DataModelId>>
}
