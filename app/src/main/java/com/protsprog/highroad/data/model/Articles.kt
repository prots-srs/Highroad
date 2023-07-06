package com.protsprog.highroad.data.model

data class ArticleAnonce(
//    val aid: Int,
//    val sort: Int?,
    val title: String,
    val description: String?,
    val picture: String?
) {
    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
//    val shortDescription: String?
//        get() = description?.smartTruncate(200)
    companion object {
        fun empty(): List<ArticleAnonce> = listOf<ArticleAnonce>(
            ArticleAnonce(
                title = "Static text demo article",
                description = "",
                picture = ""
            )
        )
    }
}