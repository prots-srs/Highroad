package com.protsprog.highroad.articles.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme

/*
//@Preview(showBackground = true, widthDp = 360)
@Preview(
    showBackground = true, device = "id:pixel_5",
    backgroundColor = 0xFFFF5599,
    widthDp = 450
)
@Composable
fun PreviewVerticalArticleCard() {
    ArticlesTheme {
        ArticleCardVertical(
            space = 16.dp,
            title = "1. Отвлечь внимание",
            picture = "https://protsprog.com/storage/articles/JwJMDBplxxrOrEyvcVvR4BjwDH2QpIMm5R9VsGBF.jpg",
//            description = "Этот метод управления гражданами можно назвать одним из основных. Правительство часто отвлекает внимание людей от реальных проблем в стране, предлагая им не очень важные сообщения. Это делается для того, чтобы не дать народу возможности интересоваться реальными проблемами в стране, которые актуальны для всех, а также развиваться, узнавать что-то новое в сферах искусства, науки и так далее. Правительству выгодно, когда люди не размышляют о серьезных вещах, а занимаются чем-то малозначительным, ведь так ими значительно легче управлять.",
        )
    }
}

 */

/*
@Preview(showBackground = true, widthDp = 600)
@Composable
fun PreviewHorizontalArticleCard() {
    ArticlesTheme {
        ArticleCardHorizontal(
            space = 24.dp,
            title = "1. Отвлечь внимание",
            picture = "https://protsprog.com/storage/articles/JwJMDBplxxrOrEyvcVvR4BjwDH2QpIMm5R9VsGBF.jpg",
            description = "Этот метод управления гражданами можно назвать одним из основных. Правительство часто отвлекает внимание людей от реальных проблем в стране, предлагая им не очень важные сообщения. Это делается для того, чтобы не дать народу возможности интересоваться реальными проблемами в стране, которые актуальны для всех, а также развиваться, узнавать что-то новое в сферах искусства, науки и так далее. Правительству выгодно, когда люди не размышляют о серьезных вещах, а занимаются чем-то малозначительным, ведь так ими значительно легче управлять.",
        )
    }
}*/

@Composable
fun ArticleCardVertical(
    space: Dp,
    title: String,
    picture: String? = null,
//    description: String? = null,
    navigateToArticle: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable { navigateToArticle() }
            .fillMaxWidth()
    ) {
        if (!picture.isNullOrEmpty()) {
            ThumbnailBox(picture)
            Spacer(Modifier.height(space))
        }

        Text(title, style = MaterialTheme.typography.bodyLarge)

//        if (!description.isNullOrEmpty()) {
//            DescriptionBox(description)
//        }

        Divider(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(top = space)
                .height(0.5.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        )
    }
}

@Composable
fun ArticleCardHorizontal(
    modifier: Modifier = Modifier,
    space: Dp,
    title: String,
    picture: String? = null,
//    description: String? = null,
    navigateToArticle: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .clickable { navigateToArticle() }
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = modifier
                    .background(Color.Blue)
                    .weight(1f)
            ) {
                ThumbnailBox(picture)
            }
//            Spacer(Modifier.width(space).fillMaxHeight().background(Color.DarkGray))

            Column(
                modifier = modifier
                    .padding(start = space)
                    .weight(2f)
            ) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)

//            if (!description.isNullOrEmpty()) {
//                Text(
//                    text = description, style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.padding(top = space)
//                )
//            }
            }
        }

        Row(modifier = modifier.fillMaxWidth()) {
            Divider(
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = space)
                    .height(0.5.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(
    showBackground = true, device = "id:pixel_5",
    backgroundColor = 0xFF995533,
    widthDp = 700
)
@Composable
fun ArticleCardHorizontalPreview() {
    ArticlesTheme {
        ArticleCardHorizontal(
            space = 24.dp,
            title = "Отвлечь внимание ,oprmkvfefk rogmnreo ebo43nvbo4mb p34",
            picture = "https://protsprog.com/storage/articles/JwJMDBplxxrOrEyvcVvR4BjwDH2QpIMm5R9VsGBF.jpg",
//            description = "Этот метод управления гражданами можно назвать одним из основных. Правительство часто отвлекает внимание людей от реальных проблем в стране, предлагая им не очень важные сообщения. Это делается для того, чтобы не дать народу возможности интересоваться реальными проблемами в стране, которые актуальны для всех, а также развиваться, узнавать что-то новое в сферах искусства, науки и так далее. Правительству выгодно, когда люди не размышляют о серьезных вещах, а занимаются чем-то малозначительным, ведь так ими значительно легче управлять.",
        )
    }
}


@Composable
fun ThumbnailBox(
    picture: String?,
    modifier: Modifier = Modifier
) {
    if (!picture.isNullOrEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(picture)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 240.dp)
            )

            if (painter.state is AsyncImagePainter.State.Loading) {
                Image(
                    painter = painterResource(id = R.drawable.ic_broken_image),
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            } else {
            }
        }
    }
}

/*
@Composable
fun DescriptionBox(
    description: String
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .height(24.dp)
                .padding(all = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            if (expanded) {
                Text(
                    "Hide more", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            } else {
                Text(
                    "Read more", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Icon(
                imageVector = if (expanded)
                    ImageVector.vectorResource(R.drawable.ic_expand_less)
                else ImageVector.vectorResource(R.drawable.ic_expand_more),
                contentDescription = if (expanded) stringResource(R.string.show_less) else stringResource(
                    R.string.show_more
                ),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }

    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(
            initialHeight = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 350, easing = LinearOutSlowInEasing)
        ),
        exit = shrinkVertically(
            targetHeight = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 350, easing = FastOutLinearInEasing)
        )
    ) {
        Spacer(Modifier.height(8.dp))
        Text(text = description, style = MaterialTheme.typography.bodyMedium)
    }
}
*/