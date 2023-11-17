package com.protsprog.highroad.todo.ui.components

/*
TO READ

https://developer.android.com/jetpack/compose/components/card
https://developer.android.com/jetpack/compose/lists
https://developer.android.com/jetpack/compose/modifiers-list#Position

https://developer.android.com/reference/kotlin/androidx/compose/foundation/lazy/LazyListState#scrollToItem(kotlin.Int,kotlin.Int)
https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/package-summary
https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
https://developer.android.com/reference/kotlin/androidx/compose/ui/focus/FocusRequester#FocusRequester()

https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/
 */
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.protsprog.highroad.R
import com.protsprog.highroad.todo.ui.theme.ToDoTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

//@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ToDoCard(
    modifier: Modifier = Modifier,
    stateTaskText: String = "",
    stateTaskClosed: Boolean = false,
    stateTaskEditing: Boolean = false,
    onTextChange: (String) -> Unit = {},
    onTextEndChange: () -> Unit = {},
    captureFocus: () -> Unit = {},
    onClickTaskEdit: () -> Unit = {},
    onClickCheckbox: (Boolean) -> Unit = {},
    deleteTask: () -> Unit = {}
) {
    val textSize = 20.sp
    val textHeight = 28.sp
    Card(
        modifier = modifier
            .fillMaxWidth()
            .swipeToDismiss(deleteTask),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
//            verticalAlignment = Alignment.Top,
//            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.padding_large))
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            Column {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(
                        modifier = modifier.padding(top = 6.dp, start = 6.dp),
                        checked = stateTaskClosed,
                        onCheckedChange = onClickCheckbox
                    )
                    Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.padding_small)))

                    if (stateTaskEditing) {

                        val focusManager = LocalFocusManager.current
                        val focusRequest = remember { FocusRequester() }
//                val keyboardController = LocalSoftwareKeyboardController.current

                        val colorLine = MaterialTheme.colorScheme.secondary
                        val paddingSmall = dimensionResource(id = R.dimen.padding_small)
                        BasicTextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(end = dimensionResource(id = R.dimen.padding_medium))
//                        .width(intrinsicSize = IntrinsicSize.Max)
                                .focusRequester(focusRequest)
                                .onFocusChanged {
                                    if (it.hasFocus) {
                                        captureFocus()
//                                keyboardController?.show()
                                    } else {
                                        onTextEndChange()
                                    }
                                }
                                .padding(
                                    top = dimensionResource(id = R.dimen.padding_medium),
                                    bottom = dimensionResource(
                                        id = R.dimen.padding_medium
                                    )
                                )
                                .drawBehind {
                                    val strokeWidth = 1 * density
                                    val y = size.height - strokeWidth / 2 + paddingSmall.roundToPx()

                                    drawLine(
                                        colorLine, Offset(0f, y), Offset(size.width, y), strokeWidth
                                    )
                                },
                            value = stateTaskText,
                            onValueChange = onTextChange,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            textStyle = TextStyle(
                                fontSize = textSize, lineHeight = textHeight
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
//                    decorationBox = @Composable { }
                        )/* off
                TextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = dimensionResource(id = R.dimen.padding_medium))
//                        .width(intrinsicSize = IntrinsicSize.Max)
                        .focusRequester(focusRequest)
                        .onFocusChanged {
//                            if (!it.hasFocus) {
//                                    onSaveNewTask()
//                                keyboardController?.show()
//                            }
                        }
                        .padding(vertical = dimensionResource(id = R.dimen.padding_small)),
                    value = stateTaskText,
                    onValueChange = onTextChange,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    textStyle = TextStyle(
                        fontSize = textSize,
                        lineHeight = textHeight
                    ),
                    shape = RectangleShape,
                    colors = colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        focusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unfocusedContainerColor = Color.Transparent,
                    )
                )*/

                        LaunchedEffect(Unit) {
                            focusRequest.requestFocus()
                        }
                    } else {
                        Text(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(end = dimensionResource(id = R.dimen.padding_medium))
//                        .width(intrinsicSize = IntrinsicSize.Max)
                                .clickable(onClick = onClickTaskEdit)
                                .padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
                            text = stateTaskText,
                            color = if (stateTaskClosed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondaryContainer,
                            textDecoration = if (stateTaskClosed) TextDecoration.LineThrough else null,
                            fontSize = textSize,
                            lineHeight = textHeight
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun ToDoCardPreviewNew() {
    ToDoTheme {
        ToDoCard(
            stateTaskText = "Lorem ipsum dolor sit amet"
        )
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun ToDoCardPreviewClosed() {
    ToDoTheme {
        ToDoCard(
            stateTaskText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            stateTaskClosed = true
        )
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun ToDoCardPreviewEditing() {
    ToDoTheme {
        ToDoCard(
            stateTaskText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            stateTaskEditing = true,
        )
    }
}

private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }

    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        val pointToAction = size.width * 3 / 4
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Touch detected; the animation should be stopped.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        // Apply the drag change to the Animatable offset.
                        // Get the drag amount change to offset the item with
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            // Instantly set the Animable to the dragOffset to ensure its moving
                            // as the user's finger moves
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                // Calculate the eventual position where the fling should settle
                //           based on the current offset value and velocity
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // Set the upper and lower bounds so that the animation stops when it
                //           reaches the edge.
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(), upperBound = size.width.toFloat()
                )

                launch {
                    // Slide back the element if the settling position does not go beyond
                    //           the size of the element. Remove the element if it does.
                    if (targetOffsetX.absoluteValue <= pointToAction) {
                        // Not enough velocity; Slide back.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                    }
                }
            }
        }
    }.offset {
        // Use the animating offset value here.
        IntOffset(offsetX.value.roundToInt(), 0)
    }
}