package com.diplomproject.model.repository

import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.data_word_request.Meanings
import com.diplomproject.model.data_description_request.DataModelId
import com.diplomproject.model.data_description_request.Definition
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_description_request.TestEnglishSample
import com.diplomproject.model.datasource.DataSource
import java.util.Collections.shuffle

const val VARIANTS = 3

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
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

    override suspend fun getDataTestEnglish(dataModels: List<DataModel>): List<Example> {
        var examples: MutableList<Example>? = mutableListOf()
        if (!dataModels.isNullOrEmpty()) {
            for (dataModel in dataModels) {
                var meanings = dataModel.meanings
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
                                    formTestCard(dataModels, it, dataModel)?.let { newExample ->
                                        examples?.add(newExample)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    continue
                }
            }
            examples?.let { shuffle(it) }
            return examples?.toList() ?: listOf()
        } else {
            return listOf()
        }
    }

    fun formTestCard(
        dataModels: List<DataModel>, definition: Definition,
        currentDataModel: DataModel
    ): Example? {
        var example: Example? = null
        val textSentence = definition.text
        if (!(textSentence.isNullOrBlank() || textSentence.isEmpty())) {
            example?.apply {
                text = definition.text
                soundUrl = definition.soundUrl
                imageUrl = currentDataModel.meanings?.get(0)?.imageUrl
                var testEnglishSamples: MutableList<TestEnglishSample> = mutableListOf()
                for (i in 0 until VARIANTS) {
                    var sampleDataModel = DataModel()
                    do {
                        sampleDataModel = dataModels[(0..(dataModels.size - 1)).random()]
                    } while (sampleDataModel.text == currentDataModel.text)

                    testEnglishSamples.add(TestEnglishSample(sampleDataModel, false) )
                }
                val rightAnswerNumber = (0..(VARIANTS - 1)).random()
                testEnglishSamples[rightAnswerNumber] = TestEnglishSample(currentDataModel, true)
                example.testEnglishSamples = testEnglishSamples
            }
        }
        return example
    }
}

