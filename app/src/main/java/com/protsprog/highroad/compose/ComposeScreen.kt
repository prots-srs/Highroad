package com.protsprog.highroad.compose

/*
READ
https://developer.android.com/courses/jetpack-compose/course

https://github.com/android/compose-samples
 */
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme

@Preview(showBackground = true)
@Composable
fun PathwayOptionPreview() {
    EntranceTheme {
        PathwayOption(
            item = composePathways[0],
            onNavigationToScreen = {}
        )
    }
}

@Composable
fun ComposeScreen(
    modifier: Modifier = Modifier,
    navigations: Map<Int, () -> Unit>
) {
    LazyColumn {
        items(composePathways) { item ->
            navigations.get(item.caseId)?.let {
                PathwayOption(
                    item = item,
                    onNavigationToScreen = it
                )
            }
        }
    }
    Spacer(Modifier.size(16.dp))
}

@Composable
fun PathwayOption(
    item: ComposePathway,
    onNavigationToScreen: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onNavigationToScreen
            )
            .semantics { contentDescription = item.title },
        shape = MaterialTheme.shapes.medium,
//        color = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = item.title,
//            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(all = 16.dp)
        )
    }
}