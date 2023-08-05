package com.diplomproject.view.description

import android.content.Context
import com.diplomproject.R
import com.diplomproject.model.data_description_request.DescriptionAppState
import com.diplomproject.model.data_description_request.Example
import com.diplomproject.model.data_word_request.Meanings
import com.diplomproject.model.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.mp.KoinPlatform.getKoin

class DescriptionInteractor(
    var repositoryRemote: Repository<List<Example>>,
) {

    suspend fun getData(
        meanings: List<Meanings>,
        fromRemoteSource: Boolean
    ): StateFlow<DescriptionAppState> {
        val descriptionAppState: DescriptionAppState
        if (fromRemoteSource) {
            descriptionAppState =
                DescriptionAppState.Success(repositoryRemote.getDataDescription(meanings))
        } else {
            val context = getKoin().get<Context>()
            descriptionAppState = DescriptionAppState.Error(
                Throwable(
                    context.getString(R.string.no_connection_internet)
                )
            )
        }
        return MutableStateFlow(descriptionAppState)
    }
}

