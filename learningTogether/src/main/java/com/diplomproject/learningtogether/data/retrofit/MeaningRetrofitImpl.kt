package  com.diplomproject.learningtogether.data.retrofit

import com.diplomproject.learningtogether.domain.entities.SkyengWordEntity
import com.diplomproject.learningtogether.domain.repos.MeaningRepo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_LOCATIONS = "https://dictionary.skyeng.ru/api/public/v1/"

class MeaningRetrofitImpl : MeaningRepo {

    private fun findImageUrlForWord(wordEntities: List<SkyengWordEntity>, word: String): String? {
        for (wordEntity in wordEntities) {
            if (wordEntity.text == word) {
                return wordEntity.meanings.getOrNull(0)?.imageUrl
            }
        }
        return null
    }

    override fun getImageUrl(word: String, onSuccess: (String?) -> Unit) {
        getService().wordAsync(word).enqueue(
            object : Callback<List<SkyengWordEntity>> {
                override fun onResponse(
                    call: Call<List<SkyengWordEntity>>,
                    response: Response<List<SkyengWordEntity>>
                ) {

//                    val wordEntities = response.body()
//
//                    if (wordEntities != null) {
//                        val imageUrl = findImageUrlForWord(wordEntities, word)
//                        val cleanedUrl = imageUrl?.replace("^.*?(https?://.*)".toRegex(), "$1")
//                        onSuccess.invoke(cleanedUrl)
//                    } else {
//                        onSuccess(null)
//                    }


                    val imageUrl = response.body()!!.first().meanings.first().imageUrl

                    val cleanedUrl = imageUrl!!.replace("^.*?(https?://.*)".toRegex(), "$1")

                    if (response.isSuccessful && cleanedUrl != null) {
                        onSuccess.invoke(cleanedUrl)
                    } else {
                        onSuccess(null)
                    }
                }

                override fun onFailure(call: Call<List<SkyengWordEntity>>, t: Throwable) {
                    onSuccess(null)
                }
            }
        )
    }

    private fun getService(): ApiService {
        return createRetrofit().create(ApiService::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATIONS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }
}