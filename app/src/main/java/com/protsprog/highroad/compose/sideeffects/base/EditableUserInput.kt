package com.protsprog.highroad.compose.sideeffects.base

/*
READ
https://developer.android.com/reference/android/os/Bundle
https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/package-summary#rememberSaveable(kotlin.Array,androidx.compose.runtime.saveable.Saver,kotlin.String,kotlin.Function0)
https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/package-summary#Saver(kotlin.Function2,kotlin.Function1)
 */
import androidx.annotation.DrawableRes
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.protsprog.highroad.compose.sideeffects.ui.captionTextStyle
import androidx.compose.ui.graphics.SolidColor

@Composable
fun CraneEditableUserInput(
    state: EditableUserInputState = rememberEditableUserInputState(hint = ""),
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
//    onInputChanged: (String) -> Unit
) {
    // Encapsulate this state in a state holder
//    var textState by remember { mutableStateOf(hint) }
//    val isHint = { textState == hint }

    CraneBaseUserInput(
        caption = caption,
        tintIcon = { !state.isHint },
        showCaption = { !state.isHint },
        vectorImageId = vectorImageId
    ) {
        BasicTextField(
            value = state.text,
            onValueChange = { state.updateText(it)},
            textStyle = if (state.isHint) {
                captionTextStyle.copy(color = LocalContentColor.current)
            } else {
                MaterialTheme.typography.body1.copy(color = LocalContentColor.current)
            },
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}

class EditableUserInputState(private val hint: String, initialText: String) {
    var text by mutableStateOf(initialText)
        private set

    fun updateText(newText: String) {
        text = newText
    }

    val isHint: Boolean
        get() = text == hint

    companion object {
        val Saver: Saver<EditableUserInputState, *> = listSaver(
            save = { listOf(it.hint, it.hint) },
            restore = { EditableUserInputState(hint = it[0], initialText = it[1]) }
        )
    }
}

@Composable
fun rememberEditableUserInputState(hint: String): EditableUserInputState =
    rememberSaveable(hint, saver = EditableUserInputState.Saver) {
        EditableUserInputState(hint, hint)
    }