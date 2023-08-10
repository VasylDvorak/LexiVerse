package com.diplomproject.model.datasource


import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.data_description_request.DataModelId
import com.diplomproject.utils.PartOfSpeech
import org.koin.java.KoinJavaComponent.getKoin


class RetrofitImplementation : DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        val getService = getKoin().get<ApiService>()
        var output = getService.searchAsync(word).await()

        output.forEach {
            it.meanings?.get(0)?.partOfSpeechCode =
                it.meanings?.get(0)?.partOfSpeechCode?.let { name ->
                    if (enumContains<PartOfSpeech>(name)) {
                        PartOfSpeech.valueOf(name).value
                    } else {
                        ""
                    }
                }
        }
        return output
    }

    inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return T::class.java.enumConstants.any { it.name == name }
    }

    override suspend fun getDataId(id: String): List<DataModelId> {
        val getService = getKoin().get<ApiService>()
        return getService.searchIdAsync(id).await()
    }

}
