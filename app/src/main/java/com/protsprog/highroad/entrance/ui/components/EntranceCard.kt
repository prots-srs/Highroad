package com.protsprog.highroad.entrance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.entrance.EntranceItem
import com.protsprog.highroad.entrance.entranceItems
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme

@Composable
fun EntranceCard(
    item: EntranceItem,
    onNavigationToScreen: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onNavigationToScreen
            ),
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
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(all = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EntranceItemPreview() {
    EntranceTheme {
        EntranceCard(
            item = entranceItems[1],
            onNavigationToScreen = {}
        )
    }
}