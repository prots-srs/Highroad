package com.protsprog.highroad.articles

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody

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
    val title: String = "demo title",
    val description: String? = "Lorem ipsum description",
    val picture: String? = null,
    val publish: Boolean = false
)

//common states
data class ServiceUiState(
    var hasServiceError: Boolean = false,
    var hasRefreshing: Boolean = false
)

data class ArticleUiState(
    var sortBySortAsc: Boolean = true,
    var showOnlyPublished: Boolean = true
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
    val id: Int,
    val picture: MultipartBody.Part,
    val publish: Boolean,
    val sort: String,
    val title: String,
    val description: String
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
    val title: String,
    val sort: String?,
    val description: String?,
    val picture: String?,
    val publish: Boolean,
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


fun ArticleService.asEntity() = ArticleEntity(
    id = id,
    title = title,
    sort = sort ?: "",
    description = description ?: "",
    picture = picture ?: "",
    publish = publish,
    createdAt = createdAt ?: "",
    updatedAt = updatedAt ?: ""
)

//to repo
//data class PermissionAnswer(
//    val can: Boolean
//)


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

