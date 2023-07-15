package com.diplomproject.di.koin_modules

import com.diplomproject.model.datasource.BaseInterceptor

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.converter.gson.GsonConverterFactory


class ApiModuleTest {

    private lateinit var apiModule: ApiModule


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
apiModule= ApiModule()
    }

    @Test
    fun createRetrofit_Test() {
        val interceptor = BaseInterceptor.interceptor
        val spyApiModule= spy(apiModule)
        spyApiModule.createRetrofit(interceptor)

        verify(spyApiModule, times(1)).baseUrlLocation()

//        val inOrder = inOrder(apiModule)
//
//        inOrder.verify(apiModule).baseUrlLocation()
//        inOrder.verify(apiModule).createOkHttpClient(interceptor)


    }
//
//    @Test
//    fun loadRecipeJavaRx_Test_ResponseIsEmpty() {
//
//        val response = Single.just(listOf<Meal>())
//        val recipe = mock(List::class.java) as List<Meal>
//        `when`(recipe.isNotEmpty()).thenReturn(false)
//        presenter.callRecipeRepo = response
//        presenter.loadRecipeJavaRx()
//        assertTrue(response.blockingGet().isEmpty())
//    }
//
//    @Test
//    fun loadRecipeJavaRx_Test_ResponseIsNotEmpty() {
//        val response =
//            Single.just(listOf(Meal(idMeal = "one"), Meal(idMeal = "two")))
//        val recipe = mock(List::class.java) as List<Meal>
//        `when`(recipe.isNotEmpty()).thenReturn(true)
//        presenter.callRecipeRepo = response
//        presenter.loadRecipeJavaRx()
//        assertNotEquals(response.blockingGet().size, 0)
//    }
//
//    @Test
//    fun onFirstViewAttach_Test() {
//        presenter.onFirstViewAttach()
//        verify(viewState, never()).init()
//    }
//
//    @Test
//    fun onDestroy_Test() {
//        presenter.onDestroy()
//        verify(viewState, never()).release()
//    }
//
//    @Test
//    fun showError_Test() {
//        presenter.showError()
//        verify(viewState, never()).apply {
//            progressCircleGone()
//            showToastFragment()
//        }
//    }
//
//    @Test
//    fun showInstructionAndIngredients_Test() {
//
//        val testRecipe = Meal()
//        testRecipe.strInstructions = "test_text"
//        presenter.currentRecipe = testRecipe
//        presenter.showInstructionAndIngredients()
//
//        val inOrder = inOrder(formIngredientsInstruction)
//        inOrder.verify(formIngredientsInstruction)
//            .formInstructionText(testRecipe.strInstructions ?: "")
//        inOrder.verify(formIngredientsInstruction).ingredientsList(testRecipe)
//    }
}
