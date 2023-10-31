package com.protsprog.highroad.glance

/*
TO READ

https://developer.android.com/reference/kotlin/androidx/glance/package-summary
https://developer.android.com/reference/java/time/format/DateTimeFormatter

https://developer.android.com/develop/ui/views/appwidgets/overview
https://developer.android.com/develop/ui/views/appwidgets#MetaData

https://developer.android.com/jetpack/androidx/releases/glance

https://github.com/android/platform-samples/tree/main/samples/user-interface/appwidgets

https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:glance/glance-appwidget/integration-tests/demos/src/main/java/androidx/glance/appwidget/demos/ActionAppWidget.kt
https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:glance/glance-appwidget/integration-tests/demos/src/main/java/androidx/glance/appwidget/demos/ResponsiveAppWidget.kt
https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:glance/glance-appwidget/integration-tests/demos/src/main/java/androidx/glance/appwidget/demos/ButtonsWidget.kt
 */
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.protsprog.highroad.MainActivity
import com.protsprog.highroad.glance.theme.HighroadWidgetGlanceColorScheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val layoutStep = 8.dp

class HighroadWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Single

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(/*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    GlanceTheme.colors
                else HighroadWidgetGlanceColorScheme.colors*/
                HighroadWidgetGlanceColorScheme.colors
            ) {
                CurrentTime()
            }
        }
    }

    @Composable
    private fun CurrentTime() {
        Column(
            modifier = GlanceModifier.fillMaxSize().padding(layoutStep * 2).appWidgetBackground()
                .background(GlanceTheme.colors.primary).cornerRadius(layoutStep),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeView()
            Spacer(modifier = GlanceModifier.height(layoutStep * 2))
            Button(
                modifier = GlanceModifier.fillMaxSize(),
                text = "Update",
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = GlanceTheme.colors.primaryContainer,
                    contentColor = GlanceTheme.colors.onPrimaryContainer
                ),
                style = TextStyle(
                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                ),
                onClick = actionRunCallback<RefreshAction>(),
                maxLines = 1
            )

        }
    }

    @Composable
    private fun TimeView() {
        val prefs = currentState<Preferences>()
        val current = prefs[stringPreferencesKey("curtime")] ?: CurrentTime.time

        Text(
            current, style = TextStyle(
                color = GlanceTheme.colors.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ), modifier = GlanceModifier.wrapContentWidth(), maxLines = 1
        )

    }

}

object CurrentTime {
    val time: String
        get() = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
            .toString()
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context, glanceId: GlanceId, parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { state ->
            state[stringPreferencesKey("curtime")] = CurrentTime.time
        }

        HighroadWidget().update(context, glanceId)
    }
}