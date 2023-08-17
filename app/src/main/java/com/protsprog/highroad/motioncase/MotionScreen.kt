package com.protsprog.highroad.motioncase

/*
TO READ
https://developer.android.com/jetpack/compose/graphics/images/customize
https://developer.android.com/jetpack/compose/touch-input/pointer-input/drag-swipe-fling
https://developer.android.com/jetpack/compose/modifiers-list#Position
https://developer.android.com/jetpack/compose/tooling/previews#ui-mode
https://developer.android.com/jetpack/compose/tooling

https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/Density#(androidx.compose.ui.unit.Dp).roundToPx()

https://www.youtube.com/watch?v=EOQB8PTLkpY&t=37s
 */
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.protsprog.highroad.R
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme
import com.protsprog.highroad.motioncase.ui.components.GameBoard
import com.protsprog.highroad.motioncase.ui.components.GameRail
import com.protsprog.highroad.motioncase.ui.components.GameTopBar
import com.protsprog.highroad.motioncase.ui.components.RestartButton
import com.protsprog.highroad.motioncase.ui.components.chipsDemo

private val stepLayout = 12.dp

@Composable
fun MotionScreen(
    onBackPressed: () -> Unit,
    windowWidthClass: WindowWidthSizeClass,
    viewModel: FifteenPuzzleViewModel = viewModel()
) {
    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }

    val localDensity = LocalDensity.current
    LaunchedEffect(Unit) {
        viewModel.prepareChipOnBoard(localDensity)
    }
    if (verticalView.value) {
        VerticalLayout(
            onBackPressed = onBackPressed,
            stateUI = viewModel.stateUIChipsOnBoard,
            currentChip = viewModel.stateUIBlockingChip,
            endGame = viewModel.stateUIEndGame,
            onRestartGame = viewModel::restartGame,
            onChangeOffset = viewModel::onChangeOffset
        )
    } else {
        HorizontalLayout(
            onBackPressed = onBackPressed,
            stateUI = viewModel.stateUIChipsOnBoard,
            currentChip = viewModel.stateUIBlockingChip,
            endGame = viewModel.stateUIEndGame,
            onRestartGame = viewModel::restartGame,
            onChangeOffset = viewModel::onChangeOffset
        )
    }
}

@Composable
fun VerticalLayout(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onRestartGame: () -> Unit = {},
    stateUI: Map<Int, IntOffset>,
    currentChip: Int,
    endGame: Boolean,
    onChangeOffset: (Int, Offset) -> Unit
) {
    Scaffold(
        topBar = {
            GameTopBar(
                title = stringResource(R.string.motioncase_app_name),
                onBackPressed = onBackPressed
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .height(stepLayout * 4)
            ) {
                AnimatedVisibility(
                    visible = endGame,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> -fullHeight },
                        animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> -fullHeight },
                        animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
                    )
                ) {
                    Text(
                        text = "Game over",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = modifier.height(stepLayout * 2))
            Row {
                GameBoard(
                    chips = stateUI,
                    currentChip = currentChip,
                    onChangeOffset = onChangeOffset
                )
            }
            Spacer(modifier = modifier.height(stepLayout * 2))
            Row {
                RestartButton(onRestartGame = onRestartGame)
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "id:pixel_4"
)
@Composable
fun VerticalLayoutPreview() {
    FlightSearchTheme {
        VerticalLayout(
            stateUI = chipsDemo,
            currentChip = 7,
            endGame = true,
            onChangeOffset = { _, _ -> }
        )
    }
}


@Composable
fun HorizontalLayout(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onRestartGame: () -> Unit = {},
    stateUI: Map<Int, IntOffset>,
    currentChip: Int,
    endGame: Boolean,
    onChangeOffset: (Int, Offset) -> Unit
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameRail(onBackPressed = onBackPressed)
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier
                        .width(stepLayout * 12)
                        .padding(vertical = 0.dp, horizontal = stepLayout),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = modifier.padding(horizontal = 0.dp, vertical = stepLayout * 2),
                        text = stringResource(R.string.motioncase_app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    AnimatedVisibility(visible = endGame) {
                        Text(
                            text = "Game over",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = modifier.width(stepLayout * 2))
                Column {
                    GameBoard(
                        chips = stateUI,
                        currentChip = currentChip,
                        onChangeOffset = onChangeOffset
                    )
                }
                Spacer(modifier = modifier.width(stepLayout * 2))
                Column {
                    RestartButton(onRestartGame = onRestartGame)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=891dp,height=411dp,dpi=420"
)
@Composable
fun HorizontalLayoutPreview() {
    FlightSearchTheme {
        HorizontalLayout(
            stateUI = chipsDemo,
            currentChip = 7,
            endGame = true,
            onChangeOffset = { _, _ -> }
        )
    }
}




