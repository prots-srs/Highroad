package com.protsprog.highroad.articles

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    @ColumnInfo(defaultValue = "")
    val sort: String,
    @ColumnInfo(defaultValue = "")
    val description: String,
    @ColumnInfo(defaultValue = "")
    val picture: String,
    val publish: Boolean,
    @ColumnInfo(defaultValue = "")
    val createdAt: String?,
    @ColumnInfo(defaultValue = "")
    val updatedAt: String?
)

fun ArticleEntity.asModel() = ArticleListModel(
    aid = id,
    publish = publish,
    sort = sort.toInt(),
    title = title,
    picture = picture
)

fun ArticleEntity.asItemModel() = ArticleItemModel(
    id = id,
    publish = publish,
    sort = sort.toInt(),
    title = title,
    description = description,
    picture = picture
)
data class ArticleListModel(
    val aid: Int = 0,
    val sort: Int = 0,
    val title: String = "",
    val description: String? = null,
    val picture: String? = null,
    val publish: Boolean = false
) {
    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
//    val shortDescription: String?
//        get() = description?.smartTruncate(200)
    companion object {
        fun exampleItem() = listOf(
            ArticleListModel(title = "Wrong loaded item")
        )
    }
}

data class ArticleItemModel(
    val id: Int = 0,
    val sort: Int = 0,
    val datetime: String = "",
    val title: String = "",
    val description: String? = "",
    val picture: String? = null,
    val publish: Boolean = false
)

fun ArticleItemModel.toPutModel() = ArticlePutModel(
    id = id,
    publish = publish,
    sort = sort.toString(),
    title = title,
    description = description ?: "",
    picture = picture?.let { Uri.parse(picture) } ?: Uri.EMPTY
)

//common states
data class ServiceUiState(
    var hasServiceError: Boolean = false,
    var hasRefreshing: Boolean = false,
    var needGoToDetailScreen: Boolean = false,
    var needGoToListScreen: Boolean = false,
    var enableSubmit: Boolean = true,
    var useMedia: Boolean = false,
    var useCamera: Boolean = false,
    var hasChangePicture: Boolean = false
)

data class ArticleUiState(
    var sortBySortAsc: Boolean = true,
    var showOnlyPublished: Boolean = false
)

//permissions states
data class PermissionsUiState(
    var update: Boolean = false,
    var delete: Boolean = false
)

//form utils
data class ArticlePutErrorsUiState(
    var common: String? = null,
    var sort: String? = null,
    var title: String? = null,
    var description: String? = null,
    var picture: String? = null
)

data class ArticlePutModel(
    val id: Int = 0,
    val picture: Uri = Uri.EMPTY,
    val publish: Boolean = false,
    val sort: String = "",
    val title: String = "",
    val description: String = ""
)

data class ArticleResponsePutModel(
    val id: Int?,
    val publish: String?,
    val sort: String?,
    val title: String?,
    val description: String?
)

//to service
@JsonClass(generateAdapter = true)
data class ArticleService(
    val id: Int,
    val publish: Boolean,
    val sort: String?,
    val title: String,
    val description: String?,
    val picture: String?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "updated_at") val updatedAt: String?
)

@JsonClass(generateAdapter = true)
data class ArticleAccess(
    val can: Boolean = false
)

@JsonClass(generateAdapter = true)
data class ArticlePutAnswer(
    val errors: Map<String, List<String>>?,
    val id: Int
)

@JsonClass(generateAdapter = true)
data class TestJsonContainer(
//    val inputs: ArticleResponsePutModel,
    // to 2 level array ( from empty arrays)
    val errors: Map<String, List<String>>?,
    // to 1 level array (empty array)
    val withoutkeysarray: List<String>?,
    val id: Int
)

@JsonClass(generateAdapter = true)
data class ArticleDeleteAnswer(
    val code: Int,
    val message: String,
    val context: Int
)


fun ArticleService.asEntity() = ArticleEntity(
    id = id,
    publish = publish,
    sort = sort ?: "",
    title = title,
    description = description ?: "",
    picture = picture ?: "",
    createdAt = createdAt ?: "",
    updatedAt = updatedAt ?: ""
)


/*@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class BooleanType

class BooleanAdapter {
    @FromJson
    @BooleanType
    fun fromJson(value: String): Boolean {
        return if (value == "true") true else false
    }

    @ToJson
    fun toJson(@BooleanType value: Boolean): String {
        return if (value) "true" else "false"
    }
}
*/

