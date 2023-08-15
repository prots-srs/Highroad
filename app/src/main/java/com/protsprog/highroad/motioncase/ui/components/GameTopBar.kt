package com.protsprog.highroad.motioncase.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.protsprog.highroad.R
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun GameTopBar(
    title: String = "",
    onBackPressed: () -> Unit = {}
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.tertiary,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(
                        R.string.bus_schedule_back
                    ),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FlightTopBarPreview() {
    FlightSearchTheme(useDarkTheme = false) {
        GameTopBar(title = stringResource(R.string.motioncase_app_name))
    }
}