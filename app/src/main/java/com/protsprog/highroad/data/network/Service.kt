package com.protsprog.highroad.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private val MY_SERVER = "https://protsprog.com/"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(MY_SERVER)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface ArticleApiService {
    @GET("article")
    suspend fun getList(): List<ServiceArticle>

//    @GET("rest.php?target=articles")
//    suspend fun getItem(@Query("id") id: Int): NetworkArticle
}

object ServiceApi {
    val articleService: ArticleApiService by lazy { retrofit.create(ArticleApiService::class.java) }
}