package com.protsprog.highroad.articles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.data.model.ArticleAnonce

@Composable
fun ArticleList(
    verticalView: Boolean,
    space: Dp,
    articles: List<ArticleAnonce>,
    modifier: Modifier = Modifier,
//    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier
            .padding(top = space, bottom = space),
        contentPadding = PaddingValues(
            horizontal = space,
            vertical = space
        ),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        items(articles) { item ->
            if (verticalView) {
                ArticleCardVertical(
                    space = space,
                    title = item.title,
                    picture = item.picture,
                    description = item.description
                )
            } else {
                ArticleCardHorizontal(
                    space = space,
                    title = item.title,
                    picture = item.picture,
                    description = item.description
                )
            }

        }
    }
}