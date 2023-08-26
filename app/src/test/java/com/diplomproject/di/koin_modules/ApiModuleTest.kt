package com.diplomproject.di.koin_modules

import com.diplomproject.model.datasource.BaseInterceptor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations


class ApiModuleTest {

    private lateinit var apiModule: ApiModule

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        apiModule = ApiModule()
    }

    @Test
    fun createRetrofit_Test() {
        val interceptor = BaseInterceptor.interceptor
        val spyApiModule = spy(apiModule)
        spyApiModule.createRetrofit(interceptor)
        val inOrder = inOrder(spyApiModule)
        inOrder.verify(spyApiModule).createOkHttpClient(interceptor)
    }

    @Test
    fun baseUrlLocation_Test() {

        Assert.assertEquals(
            apiModule.baseUrl,
            "https://dictionary.skyeng.ru/api/public/v1/"
        )
    }
}
