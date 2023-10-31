package com.protsprog.highroad.glance

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.Switch
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.ToggleableStateKey
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.protsprog.highroad.MainActivity
import com.protsprog.highroad.R

/**
 * Glance widget that showcases how to use:
 * - Actions
 * - Compound buttons
 * - Buttons
 * - AndroidRemoteView
 */
class ButtonsGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @SuppressLint("RemoteViewLayout")
    @Composable
    fun Content() {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.background)
                    .appWidgetBackgroundCornerRadius(),
            ) {
                Text(
                    text = LocalContext.current.getString(R.string.glance_buttons_title),
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                )
                LazyColumn {
                    item {
                        Button(
                            text = "Button",
                            modifier = GlanceModifier.fillMaxWidth(),
                            onClick = actionStartActivity<MainActivity>(),
                        )
                    }
                    item {
                        CheckBox(
                            text = "Checkbox",
                            checked = currentState(key = CheckboxKey) ?: false,
                            onCheckedChange = actionRunCallback<CompoundButtonAction>(
                                actionParametersOf(SelectedKey to CheckboxKey.name),
                            ),
                        )
                    }
                    item {
                        Switch(
                            text = "Switch",
                            checked = currentState(key = SwitchKey) ?: false,
                            onCheckedChange = actionRunCallback<CompoundButtonAction>(
                                actionParametersOf(SelectedKey to SwitchKey.name),
                            ),
                        )
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        item {
                            // Radio buttons are not implemented yet in Glance, using the interop
                            // composable to use the RemoteView + XML
                            AndroidRemoteViews(
                                remoteViews = RemoteViews(
                                    LocalContext.current.packageName,
                                    R.layout.item_radio_buttons,
                                ).apply {
                                    // This code will check the item_radio_button2 in the
                                    // item_radio_group RadioGroup
                                    setRadioGroupChecked(
                                        R.id.item_radio_group,
                                        R.id.item_radio_button2,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

private val CheckboxKey = booleanPreferencesKey("checkbox")
private val SwitchKey = booleanPreferencesKey("switch")
private val SelectedKey = ActionParameters.Key<String>("key")

class CompoundButtonAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        // The framework automatically sets the value of the toggled action (true/false)
        // Retrieve it using the ToggleableStateKey
        val toggled = parameters[ToggleableStateKey] ?: false
        updateAppWidgetState(context, glanceId) { prefs ->
            // Get which button the action came from
            val key = booleanPreferencesKey(parameters[SelectedKey] ?: return@updateAppWidgetState)
            // Update the state
            prefs[key] = toggled
        }
        ButtonsGlanceWidget().update(context, glanceId)
    }
}

class ButtonsGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ButtonsGlanceWidget()
}