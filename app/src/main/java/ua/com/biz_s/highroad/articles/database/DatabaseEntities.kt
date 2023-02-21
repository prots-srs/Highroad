package ua.com.biz_s.highroad.articles.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import ua.com.biz_s.highroad.articles.domain.Article

@Entity(tableName = "articles")
data class DatabaseArticle constructor(
    @PrimaryKey val id: Int = 0,
    val title: String,
    val sort: Int?,
    val description: String?,
    @ColumnInfo(name = "picture") val thumbnail: String?
)

fun List<DatabaseArticle>.asDomainModel(): List<Article> {
    return map {
        Article(
            id = it.id,
            title = it.title,
            sort = it.sort ?: 0,
            description = it.description ?: "",
            thumbnail = it.thumbnail ?: ""
        )
    }
}