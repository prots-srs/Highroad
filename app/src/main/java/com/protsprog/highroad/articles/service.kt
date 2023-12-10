package com.protsprog.highroad.articles

/*
TO READ

https://www.zacsweers.dev/exploring-moshis-kotlin-code-gen/

https://square.github.io/retrofit/

https://medium.com/javarevisited/10-rest-api-best-practices-cd12e3904d00
 */
import com.protsprog.highroad.util.API_SERVER
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(API_SERVER)
    .build()

interface ArticleApiService {
    @GET("api/article")
    suspend fun getList(): List<ArticleService>

    @GET("api/article/{id}")
    suspend fun getItem(@Path("id") id: Int): ArticleService

    @FormUrlEncoded
    @POST("api/access")
    suspend fun getAccess(
        @Header("Authorization") token: String,
        @Field("theme") theme: String = "article",
        @Field("operation") operation: String
    ): Response<ArticleAccess>

    @Multipart
    @POST("api/article")
    suspend fun putItem(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("image") requestBody: RequestBody,
        @Part("id") id: RequestBody,
        @Part("publish") publish: RequestBody,
        @Part("sort") sort: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
    ): Response<ArticlePutAnswer>

    @Multipart
    @POST("api/put-test")
    suspend fun testPutItem(
        @Part("id") id: RequestBody,
//        @Part("publish") publish: RequestBody,
//        @Part("sort") sort: RequestBody,
//        @Part("title") title: RequestBody,
//        @Part("description") description: RequestBody,
    ): Response<TestJsonContainer>

}

object ServiceApi {
    val articleService: ArticleApiService by lazy { retrofit.create(ArticleApiService::class.java) }
}