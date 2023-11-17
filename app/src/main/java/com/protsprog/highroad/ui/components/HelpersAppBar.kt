package com.protsprog.highroad.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R

@Composable
fun AppBarTitle(title: String = "") {
    Column {
        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun AppBarMenuTitle(title: String = "") {
    Text(
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun AppBarBackIcon(onBackPressed: () -> Unit) {
    IconButton(
        onClick = onBackPressed,
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.theming_back_button),
            modifier = Modifier.size(dimensionResource(id = R.dimen.padding_large))
        )
    }
}

@Composable
fun AppBarMenuIcon(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier//.padding(dimensionResource(id = R.dimen.padding_small)),
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Content menu",
            modifier = Modifier.size(dimensionResource(id = R.dimen.padding_large))
        )
    }
}