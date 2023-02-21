package ua.com.biz_s.highroad.articles.domain

import ua.com.biz_s.highroad.articles.util.smartTruncate

data class Article(
    val id: Int,
    val title: String,
    val sort: Int?,
    val description: String?,
    val thumbnail: String?
) {
    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
//    val shortDescription: String?
//        get() = description?.smartTruncate(200)
}