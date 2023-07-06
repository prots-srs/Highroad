/*
to read
https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-api-guidelines.md#elements-accept-and-respect-a-modifier-parameter
https://developer.android.com/jetpack/compose/modifiers
https://developer.android.com/jetpack/compose/modifiers-list
 */
package com.protsprog.highroad.compose.basiclayouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.compose.basiclayouts.theme.BasicLayoutsTheme
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.protsprog.highroad.R
import java.util.Locale

@Composable
fun BasicLayoutsApp(modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = { SootheBottomNavigation() }
    ) { padding -> HomeScreen(Modifier.padding(padding)) }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun BasicLayoutsPreview(modifier: Modifier = Modifier) {
    BasicLayoutsTheme { BasicLayoutsApp() }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text(stringResource(R.string.basiclayout_placeholder_search))
        }
    )
}

/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, widthDp = 360)
@Composable
fun SearchBarPreview() {
    BasicLayoutsTheme { SearchBar(Modifier.padding(8.dp)) }
}

 */


@Composable
private fun AlignYourBodyElement(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    @StringRes text: Int
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier
                .size(88.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = painterResource(drawable),
            contentDescription = null
        )
        Text(
            modifier = modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
            text = stringResource(text),
//            color = Color(0xFF8BC34A)
        )
    }
}

/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
private fun AlignYourBodyElementPreview() {
    BasicLayoutsTheme {
        AlignYourBodyElement(
            modifier = Modifier.padding(8.dp),
            text = R.string.basiclayout_ab1_inversions,
            drawable = R.drawable.basiclayout_ab1_inversions,
        )
    }
}

 */


@Composable
fun FavoriteCollectionCard(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    @StringRes text: Int
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.width(192.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(drawable),
                contentDescription = null
            )
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
fun FavoriteCollectionCardPreview() {
    BasicLayoutsTheme {
        FavoriteCollectionCard(
            modifier = Modifier.padding(8.dp),
            text = R.string.basiclayout_fc2_nature_meditations,
            drawable = R.drawable.basiclayout_fc2_nature_meditations,
        )
    }
}

 */


@Composable
private fun AlignYourBodyRow(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(alignYourBodyData) { item ->
            AlignYourBodyElement(drawable = item.drawable, text = item.text)
        }
    }
}

/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
private fun AlignYourBodyRowPreview() {
    BasicLayoutsTheme { AlignYourBodyRow() }
}
*/


@Composable
private fun FavoriteCollectionsGrid(
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        modifier = modifier.height(120.dp),
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(favoriteCollectionsData) { item ->
            FavoriteCollectionCard(
                drawable = item.drawable,
                text = item.text,
                modifier = Modifier.height(56.dp)
            )
        }
    }
}

/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
private fun FavoriteCollectionsGridPreview() {
    BasicLayoutsTheme { FavoriteCollectionsGrid() }
}
*/


@Composable
private fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 8.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}
/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
private fun HomeSectionPreview() {
    BasicLayoutsTheme {
        HomeSection(R.string.basiclayout_align_your_body) {
            AlignYourBodyRow()
        }
    }
}
 */


@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    val scrolableState = rememberScrollState()
    Column(
        modifier
            .verticalScroll(scrolableState)
            .padding(vertical = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))
        HomeSection(title = R.string.basiclayout_align_your_body) {
            AlignYourBodyRow()
        }
        HomeSection(title = R.string.basiclayout_favorite_collections) {
            FavoriteCollectionsGrid()
        }
        Spacer(Modifier.height(16.dp))
    }
}
/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, heightDp = 180)
@Composable
private fun ScreenContentPreview() {
    BasicLayoutsTheme { HomeScreen() }
}
*/


@Composable
private fun SootheBottomNavigation(modifier: Modifier = Modifier) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_spa),
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.basiclayout_bottom_navigation_home))
            },
            selected = true,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.basiclayout_bottom_navigation_profile))
            },
            selected = true,
            onClick = {}
        )
    }
}
/*
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
@Composable
private fun BottomNavigationPreview() {
    BasicLayoutsTheme { SootheBottomNavigation(Modifier.padding(top = 24.dp)) }
}*/


/*
simple data
 */
private val alignYourBodyData = listOf(
    R.drawable.basiclayout_ab1_inversions to R.string.basiclayout_ab1_inversions,
    R.drawable.basiclayout_ab2_quick_yoga to R.string.basiclayout_ab2_quick_yoga,
    R.drawable.basiclayout_ab3_stretching to R.string.basiclayout_ab3_stretching,
    R.drawable.basiclayout_ab4_tabata to R.string.basiclayout_ab4_tabata,
    R.drawable.basiclayout_ab5_hiit to R.string.basiclayout_ab5_hiit,
    R.drawable.basiclayout_ab6_pre_natal_yoga to R.string.basiclayout_ab6_pre_natal_yoga
).map { DrawableStringPair(it.first, it.second) }

private val favoriteCollectionsData = listOf(
    R.drawable.basiclayout_fc1_short_mantras to R.string.basiclayout_fc1_short_mantras,
    R.drawable.basiclayout_fc2_nature_meditations to R.string.basiclayout_fc2_nature_meditations,
    R.drawable.basiclayout_fc3_stress_and_anxiety to R.string.basiclayout_fc3_stress_and_anxiety,
    R.drawable.basiclayout_fc4_self_massage to R.string.basiclayout_fc4_self_massage,
    R.drawable.basiclayout_fc5_overwhelmed to R.string.basiclayout_fc5_overwhelmed,
    R.drawable.basiclayout_fc6_nightly_wind_down to R.string.basiclayout_fc6_nightly_wind_down
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)