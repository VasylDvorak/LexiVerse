package com.diplomproject.model.datasource

import com.diplomproject.model.data.DataModel


interface DataSource<T> {

    suspend fun getData(word: String): List<DataModel>

}
