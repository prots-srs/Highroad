package com.protsprog.highroad.articles.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme

@Composable
fun ArticleDetailItem(
    modifier: Modifier = Modifier,
    title: String,
    picture: String?,
    description: String?,
    publish: Boolean = true
) {
    Column {
        Text(
            title,
            fontWeight = FontWeight.Bold, fontSize = 26.sp
        )
        if (publish) {
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
            Icon(Icons.Outlined.Visibility, "Publish")
        }
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_large)))
        if (!picture.isNullOrEmpty()) {
            ThumbnailBox(picture)
            Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_large)))
        }
        description?.let {
            Text(text = it, fontSize = 20.sp)
        }
    }
}

@Preview(
//    showBackground = true,
    device = "id:pixel_5",
    backgroundColor = 0xFF995533,
)
@Composable
fun ArticleDetailItemPreview() {
    ArticlesTheme {
        ArticleDetailItem(
            title = "lorem ipsum lorem ipsum 234",
            picture = "https://protsprog.com/storage/articles/JwJMDBplxxrOrEyvcVvR4BjwDH2QpIMm5R9VsGBF.jpg",
            description = "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsumlorem ipsum"
        )
    }
}
