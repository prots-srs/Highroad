package com.protsprog.highroad.compose.sideeffects

/*
READ
https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary#snapshotFlow(kotlin.Function0)
https://developer.android.com/kotlin/flow
 */
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.protsprog.highroad.compose.sideeffects.base.CraneEditableUserInput
import com.protsprog.highroad.compose.sideeffects.base.CraneUserInput
import com.protsprog.highroad.compose.sideeffects.PeopleUserInputAnimationState.Invalid
import com.protsprog.highroad.compose.sideeffects.PeopleUserInputAnimationState.Valid
import com.protsprog.highroad.compose.sideeffects.ui.CraneTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.protsprog.highroad.R
import com.protsprog.highroad.compose.sideeffects.base.rememberEditableUserInputState
import kotlinx.coroutines.flow.filter

enum class PeopleUserInputAnimationState { Valid, Invalid }

class PeopleUserInputState {
    var people by mutableStateOf(1)
        private set

    val animationState: MutableTransitionState<PeopleUserInputAnimationState> =
        MutableTransitionState(Valid)

    fun addPerson() {
        people = (people % (MAX_PEOPLE + 1)) + 1
        updateAnimationState()
    }

    private fun updateAnimationState() {
        val newState =
            if (people > MAX_PEOPLE) Invalid
            else Valid

        if (animationState.currentState != newState) animationState.targetState = newState
    }
}

@Composable
fun PeopleUserInput(
    titleSuffix: String? = "",
    onPeopleChanged: (Int) -> Unit,
    peopleState: PeopleUserInputState = remember { PeopleUserInputState() }
) {
    Column {
        val transitionState = remember { peopleState.animationState }
        val tint = tintPeopleUserInput(transitionState)

        val people = peopleState.people
        CraneUserInput(
            text = if (people == 1) "$people Adult$titleSuffix" else "$people Adults$titleSuffix",
            vectorImageId = R.drawable.ic_person,
            tint = tint.value,
            onClick = {
                peopleState.addPerson()
                onPeopleChanged(peopleState.people)
            }
        )
        if (transitionState.targetState == Invalid) {
            Text(
                text = "Error: We don't support more than $MAX_PEOPLE people",
                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondary)
            )
        }
    }
}

@Composable
fun FromDestination() {
    CraneUserInput(text = "Seoul, South Korea", vectorImageId = R.drawable.ic_location)
}

@Composable
fun ToDestinationUserInput(onToDestinationChanged: (String) -> Unit) {
    val editableUserInputState = rememberEditableUserInputState(hint = "Choose Destination")
    CraneEditableUserInput(
        state = editableUserInputState,
        caption = "To",
        vectorImageId = R.drawable.ic_plane
    )

    val currentOnDestinationChanged by rememberUpdatedState(newValue = onToDestinationChanged)
    LaunchedEffect(editableUserInputState) {
        snapshotFlow { editableUserInputState.text }
            .filter { !editableUserInputState.isHint }
            .collect {
                currentOnDestinationChanged(editableUserInputState.text)
            }
    }
}

@Composable
fun DatesUserInput() {
    CraneUserInput(
        caption = "Select Dates",
        text = "",
        vectorImageId = R.drawable.ic_calendar
    )
}

@Composable
private fun tintPeopleUserInput(
    transitionState: MutableTransitionState<PeopleUserInputAnimationState>
): State<Color> {
    val validColor = MaterialTheme.colors.onSurface
    val invalidColor = MaterialTheme.colors.secondary

    val transition = updateTransition(transitionState, label = "")
    return transition.animateColor(
        transitionSpec = { tween(durationMillis = 300) }, label = ""
    ) {
        if (it == Valid) validColor else invalidColor
    }
}

@Preview
@Composable
fun PeopleUserInputPreview() {
    CraneTheme {
        PeopleUserInput(onPeopleChanged = {})
    }
}
