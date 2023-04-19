package ua.com.biz_s.highroad.articles.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.com.biz_s.highroad.articles.database.DatabaseArticle

@JsonClass(generateAdapter = true)
data class NetworkArticleContainer(
    val items: List<NetworkArticle>,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class NetworkArticle(
    val id: Int,
    val title: String,
    val sort: Int?,
    val description: String?,
    @Json(name = "picture") val thumbnail: String?
)

/*fun NetworkArticleContainer.asDomainModel(): List<Article> {
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

fun NetworkArticleContainer.asDatabaseModel(): List<DatabaseArticle> {
    return items.map {
        DatabaseArticle(
            id = it.id,
            title = it.title,
            sort = it.sort ?: 0,
            description = it.description ?: "",
            thumbnail = it.thumbnail ?: ""
        )
    }
}
fun List<NetworkArticle>.asDatabaseModel(): List<DatabaseArticle> {
    return map {
        DatabaseArticle(
            id = it.id,
            title = it.title,
            sort = it.sort ?: 0,
            description = it.description ?: "",
            thumbnail = it.thumbnail ?: ""
        )
    }
}

fun NetworkArticle.asDatabaseModel(): DatabaseArticle {
    return DatabaseArticle(
        id = this.id,
        title = this.title,
        sort = this.sort ?: 0,
        description = this.description ?: "",
        thumbnail = this.thumbnail ?: ""
    )
}