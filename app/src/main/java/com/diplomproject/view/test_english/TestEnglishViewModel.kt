package com.diplomproject.view.test_english

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.test_english_request.TestEnglishAppState
import com.diplomproject.utils.parseSearchResultsTestEnglish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val QUERY_TESTS = "query_tests"

class TestEnglishViewModel(var interactor: TestEnglishInteractor) : ViewModel() {

    var _liveDataForTestEnglishViewToObserve: MutableLiveData<TestEnglishAppState> =
        MutableLiveData()
    private val liveDataForViewToObserve: LiveData<TestEnglishAppState> =
        _liveDataForTestEnglishViewToObserve
    var savedStateHandle: SavedStateHandle = SavedStateHandle()
    protected var queryStateFlow = MutableStateFlow(Pair(listOf<DataModel>(), true))
    var coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun subscribe(): LiveData<TestEnglishAppState> {
        return liveDataForViewToObserve
    }

    fun getRestoredData(): TestEnglishAppState? = savedStateHandle[QUERY_TESTS]
    fun setQuery(query: TestEnglishAppState) {
        savedStateHandle[QUERY_TESTS] = query
    }

    public override fun onCleared() {
        _liveDataForTestEnglishViewToObserve.value = TestEnglishAppState.Success(null)
        super.onCleared()
        cancelJob()
    }


    fun getDataForTests(
        listDataModels: List<DataModel>,
        isOnline: Boolean
    ): LiveData<TestEnglishAppState> {

        queryStateFlow.value = Pair(listDataModels, isOnline)
        coroutineScope.launch {
            queryStateFlow.filter { query ->
                if (query.first.isEmpty()) {
                    _liveDataForTestEnglishViewToObserve.postValue(
                        TestEnglishAppState
                            .Error(Throwable("Пустая строка"))
                    )
                    return@filter false
                } else {
                    return@filter true
                }
            }
                // .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emit(TestEnglishAppState.Error(Throwable("Ошибка")))
                        }
                }
                .filterNotNull()
                .collect { result ->
                    result
                    _liveDataForTestEnglishViewToObserve.postValue(result)
                }
        }

        return _liveDataForTestEnglishViewToObserve
    }

    fun dataFromNetwork(query: Pair<List<DataModel>, Boolean>): Flow<TestEnglishAppState> {
        return flow {
            emit(
                parseSearchResultsTestEnglish(
                    interactor.getData(
                        query.first,
                        query.second
                    )
                )
            )
        }
    }


    protected fun cancelJob() {
        queryStateFlow = MutableStateFlow(Pair(listOf(), true))
    }
}