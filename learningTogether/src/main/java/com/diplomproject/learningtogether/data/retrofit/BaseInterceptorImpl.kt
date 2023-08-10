package com.diplomproject.learningtogether.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Для перехвата основных ответов и отображения ошибок пользователю
 * не полностью реализовано
 */
class BaseInterceptorImpl private constructor() : Interceptor {

    private var responseCode: Int = 0

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        responseCode = response.code
        return response
    }

    companion object {

        val interceptor: BaseInterceptorImpl
            get() = BaseInterceptorImpl()
    }
}