package  com.diplomproject.learningtogether.data.retrofit

import com.diplomproject.learningtogether.domain.entities.SkyengWordEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Deferred - это интекфейс который позволяет дождатся выполнение некоторого кода.
 */
interface ApiService {

    // https://dictionary.skyeng.ru/api/public/v1/

//    @GET("words/search")
//    fun searchAsync(
//        @Query("search") wordToSearch: String
//    ): Deferred<List<DataEntity>>

    // https://dictionary.skyeng.ru/api/public/v1/words/search?search=dog
    @GET("words/search")
    fun wordAsync(
        @Query("search") word: String?
    ): Call<List<SkyengWordEntity>>
}