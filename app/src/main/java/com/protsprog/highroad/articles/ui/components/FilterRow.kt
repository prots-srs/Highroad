package com.protsprog.highroad.articles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme

@Composable
fun FilterRow(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            modifier = modifier.semantics { contentDescription = "Sorting" },
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier.width(dimensionResource(id = R.dimen.padding_medium)))
        Text(text = label)
    }
}

@Preview(showBackground = true, device = "id:pixel_5", backgroundColor = 0xFFFF5599)
@Composable
fun FilterViewPreview() {
    ArticlesTheme {
        FilterRow(
            label = "Sorting by sort",
            checked = true
        )
    }
}

