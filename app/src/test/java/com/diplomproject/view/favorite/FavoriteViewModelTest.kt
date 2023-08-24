package com.diplomproject.view.favorite

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.runner.AndroidJUnit4
import com.google.gson.Gson
import com.diplomproject.TestCoroutineRule
import com.diplomproject.model.datasource.AppState
import com.diplomproject.model.data_word_request.DataModel
import com.diplomproject.model.data_word_request.Meanings
import com.diplomproject.model.repository.RepositoryLocal
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28], manifest = Config.NONE)
@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var favoriteViewModel: FavoriteViewModel


    private lateinit var interactor: FavoriteInteractor


    @Mock
    private lateinit var repositoryLocal: RepositoryLocal<List<DataModel>>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = FavoriteInteractor(repositoryLocal)
        favoriteViewModel = FavoriteViewModel(interactor).apply {

            coroutineScope = CoroutineScope(
                Dispatchers.Main
                        + SupervisorJob()
                        + CoroutineExceptionHandler { _, throwable ->
                    favoriteViewModel.handleError(throwable)
                })
        }
    }

    @Test
    fun coroutines_MainRequest_TestReturnValueIsEquals() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = favoriteViewModel.subscribe()

            try {
                liveData.observeForever(observer)

                val answer = listOf(
                    DataModel(text = "first",  meanings =  listOf(Meanings(imageUrl = "firstImage"))),
                    DataModel(text = "first", meanings =   listOf(Meanings(imageUrl = "secondImage")))
                )

                `when`(interactor.repositoryLocal.getFavoriteList()).thenReturn(answer)

                favoriteViewModel.getData(SEARCH_QUERY, false)

                Assert.assertNotNull(liveData)

                when (liveData.value) {
                    is AppState.Success -> {
                        val value = liveData.value as AppState.Success
                        value.data?.let {
                            Assert.assertTrue(
                                Gson().toJson(it)
                                    .equals(Gson().toJson(answer))
                            )
                        }

                    }

                    else -> {
                        Assert.assertTrue(false)
                    }
                }

            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutines_remove() {

        favoriteViewModel.interactor = mock(FavoriteInteractor::class.java)
        val sendToFavorite = DataModel(text = "first", meanings =  listOf(Meanings(imageUrl = "firstImage")))

        testCoroutineRule.runBlockingTest {
            favoriteViewModel.remove(sendToFavorite)

            val inOrder = inOrder(favoriteViewModel.interactor)
            inOrder.verify(favoriteViewModel.interactor).removeFavoriteItem(sendToFavorite)
            inOrder.verify(favoriteViewModel.interactor).getData("", true)
        }
    }
    @Test
    fun test_onCleared(){
        val observerAppState: Observer<AppState?>? = Observer {}
        val liveDataAppState: LiveData<AppState>? = favoriteViewModel.subscribe()

        try {
            observerAppState?.let { liveDataAppState?.observeForever(it) }

            favoriteViewModel.onCleared()

            when (liveDataAppState?.value) {
                is AppState.Success -> {
                    val value = liveDataAppState.value as AppState.Success
                    Assert.assertNull(value.data)
                }
                else -> {
                    Assert.assertTrue(false)
                }
            }

        } finally {
            observerAppState?.let { liveDataAppState?.removeObserver(it) }
        }

    }

    @Test
    fun test_handleError() {
        val observerAppState: Observer<AppState?>? = Observer {}
        val liveDataAppState: LiveData<AppState>? = favoriteViewModel.subscribe()

        try {
            observerAppState?.let { liveDataAppState?.observeForever(it) }
            val answer = Throwable(EXCEPTION_TEXT)
            favoriteViewModel.handleError(answer)

            when (liveDataAppState?.value) {
                is AppState.Error -> {
                    val value = liveDataAppState.value as AppState.Error
                    Assert.assertEquals(value.error.message, answer.message)
                }

                else -> {
                    Assert.assertTrue(false)
                }
            }
        } finally {
            observerAppState?.let { liveDataAppState?.removeObserver(it) }
        }
    }
    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val EXCEPTION_TEXT = "Ошибка"
    }
}
