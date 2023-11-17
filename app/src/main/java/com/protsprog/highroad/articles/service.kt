package com.protsprog.highroad.articles

/*
READ
https://www.zacsweers.dev/exploring-moshis-kotlin-code-gen/
 */
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
data class ArticleService(
    val id: Int,
    val title: String,
    val sort: String?,
    val description: String?,
    val picture: String?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "updated_at") val updatedAt: String?
)

fun ArticleService.asEntity() = ArticleEntity(
    id = id,
    title = title,
    sort = sort ?: "",
    description = description ?: "",
    picture = picture ?: ""
)

fun ArticleService.asModel() = ArticleListModel(
    aid = id,
    sort = sort?.toInt() ?: 0,
    title = title,
    description = description,
    picture = picture
)

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
    suspend fun getList(): List<ArticleService>

//    @GET("rest.php?target=articles")
//    suspend fun getItem(@Query("id") id: Int): NetworkArticle
}

object ServiceApi {
    val articleService: ArticleApiService by lazy { retrofit.create(ArticleApiService::class.java) }
}