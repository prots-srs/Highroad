package com.protsprog.highroad.data.network

/*
READ
https://www.zacsweers.dev/exploring-moshis-kotlin-code-gen/
 */
import com.protsprog.highroad.data.local.database.ArticleEntity
import com.protsprog.highroad.data.model.ArticleAnonce
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServiceArticle(
    val id: Int,
    val title: String,
    val sort: String?,
    val description: String?,
    val picture: String?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "updated_at") val updatedAt: String?
)

fun ServiceArticle.asEntity() = ArticleEntity(
    id = id,
    title = title,
    sort = sort ?: "",
    description = description ?: "",
    picture = picture ?: ""
)

fun ServiceArticle.asExternalModel() = ArticleAnonce(
//    id = this.id,
//    sort = this.sort ?: 0,
    title = this.title,
    description = this.description ?: "",
    picture = this.picture ?: ""
)


/*
@JsonClass(generateAdapter = true)
data class NetworkArticleContainer(
    val items: List<NetworkArticle>,
    val title: String?
)

fun NetworkArticleContainer.asDomainModel(): List<Article> {
    return items.map {
        Article(
            id = it.id,
            title = it.title,
            sort = it.sort,
            description = it.description ?: "",
            thumbnail = it.thumbnail ?: ""
        )
    }
}*/


