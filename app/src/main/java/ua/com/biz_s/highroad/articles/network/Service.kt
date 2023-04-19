package ua.com.biz_s.highroad.articles.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://protsprog.com/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface ArticleApiService {
    @GET("article")
//    suspend fun getList(): NetworkArticleContainer
    suspend fun getList(): List<NetworkArticle>

    @GET("article/{id}")
    suspend fun getItem(@Path("id") id: Int): NetworkArticle
}

object ArticlesApi {
    val service: ArticleApiService by lazy { retrofit.create(ArticleApiService::class.java) }
}