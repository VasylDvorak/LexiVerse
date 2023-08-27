package com.diplomproject.model.repository

import com.diplomproject.model.data_description_request.DataModelId
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.data_word_request.Meanings

class RepositoryImplementation(private val dataSource: RepositoryDataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    suspend fun getDataId(id: Int): List<DataModelId> {
        return dataSource.getDataId(id.toString())
    }


    override suspend fun getFavoriteList(): List<DataModel> = listOf()
    override suspend fun getDataDescription(meanings: List<Meanings>): List<Example> {
        var examples: ArrayList<Example>? = arrayListOf()
        if (!meanings.isNullOrEmpty()) {
            meanings.forEach { meaning ->
                //  DataModel.meanings?.get(0)?.let { meaning ->
                var dataModelsId = meaning.id?.let { getDataId(it) }
                if (!dataModelsId.isNullOrEmpty()) {
                    dataModelsId.forEach { dataModelId ->
                        if (!dataModelId.examples.isNullOrEmpty()) {
                            dataModelId.examples.let { examples?.addAll(it) }
                        }
                        dataModelId.definition?.let {
                            examples?.add(
                                Example(
                                    soundUrl = it.soundUrl,
                                    text = it.text
                                )
                            )
                        }
                    }
                }
            }
            return examples?.toList() ?: listOf()
        } else {
            return listOf()
        }

    }

}

