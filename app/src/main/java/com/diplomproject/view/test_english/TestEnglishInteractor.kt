package com.diplomproject.view.test_english

import android.content.Context
import com.diplomproject.R
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.repository.Repository
import com.diplomproject.model.test_english_request.TestEnglishAppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.mp.KoinPlatform.getKoin

class TestEnglishInteractor(
    var repositoryRemote: Repository<List<Example>>,
) {

    suspend fun getData(
        dataModels: List<DataModel>,
        fromRemoteSource: Boolean
    ): StateFlow<TestEnglishAppState> {
        val testEnglishAppState: TestEnglishAppState
        if (fromRemoteSource) {
            testEnglishAppState =
                TestEnglishAppState.Success(repositoryRemote.getDataTestEnglish(dataModels))
        } else {
            val context = getKoin().get<Context>()
            testEnglishAppState = TestEnglishAppState.Error(
                Throwable(
                    context.getString(R.string.no_connection_internet)
                )
            )
        }
        return MutableStateFlow(testEnglishAppState)
    }
}

