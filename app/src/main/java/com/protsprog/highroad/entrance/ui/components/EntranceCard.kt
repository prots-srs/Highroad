package com.protsprog.highroad.entrance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.entrance.EntranceItem
import com.protsprog.highroad.entrance.entranceItems
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewVerticalEntranceCard() {
    EntranceTheme {
        EntranceCardVertical(
            item = entranceItems[1],
            onNavigationToScreen = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 600)
@Composable
fun PreviewHorizontalEntranceCard() {
    EntranceTheme {
        EntranceCardHorizontal(
            item = entranceItems[1],
            onNavigationToScreen = {}
        )
    }
}

@Composable
fun EntranceCardVertical(
    item: EntranceItem,
    onNavigationToScreen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigationToScreen)
            .semantics { contentDescription = item.title },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Image(
            painter = painterResource(item.picture),
            contentDescription = item.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Text(
            text = item.title,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(all = 16.dp)
        )
    }
}

@Composable
fun EntranceCardHorizontal(
    item: EntranceItem,
    onNavigationToScreen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigationToScreen)
            .semantics { contentDescription = item.title },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Image(
                painter = painterResource(item.picture),
                contentDescription = item.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(320.dp)
                    .padding(end = 16.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}