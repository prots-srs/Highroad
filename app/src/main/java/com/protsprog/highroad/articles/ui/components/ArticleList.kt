package com.protsprog.highroad.articles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ArticleListModel

@Composable
fun ArticleList(
    verticalView: Boolean,
    step: Int,
    articles: List<ArticleListModel>,
    modifier: Modifier = Modifier,
    navigateToArticle: (Int) -> Unit,
//    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier.padding(all = 0.dp),
//        contentPadding = PaddingValues(
//            horizontal = dimensionResource(id = R.dimen.padding_small) * step,
//            vertical = dimensionResource(id = R.dimen.padding_small) * step
//        ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small) * step)
    ) {
        items(articles) { item ->
            if (verticalView) {
                ArticleCardVertical(
                    space = dimensionResource(id = R.dimen.padding_small) * step,
                    publish = item.publish,
                    title = item.title,
                    picture = item.picture,
//                    description = item.description,
                    navigateToArticle = { navigateToArticle(item.aid) }
                )
            } else {
                ArticleCardHorizontal(
                    space = dimensionResource(id = R.dimen.padding_small) * step,
                    publish = item.publish,
                    title = item.title,
                    picture = item.picture,
//                    description = item.description,
                    navigateToArticle = { navigateToArticle(item.aid) }
                )
            }

        }
    }
}