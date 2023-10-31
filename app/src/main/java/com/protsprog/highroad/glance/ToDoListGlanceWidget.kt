package com.protsprog.highroad.glance

/*
TO READ
https://developer.android.com/jetpack/compose/glance

https://developer.android.com/reference/kotlin/androidx/glance/appwidget/lazy/package-summary#(androidx.glance.appwidget.lazy.LazyListScope).items(kotlin.collections.List,kotlin.Function1,kotlin.Function2)
https://developer.android.com/reference/kotlin/androidx/datastore/preferences/core/package-summary#stringSetPreferencesKey(kotlin.String)
https://developer.android.com/reference/kotlin/androidx/datastore/preferences/core/Preferences.Key

https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/mutable-set-of.html
https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/

https://developer.android.com/codelabs/android-preferences-datastore
https://developer.android.com/codelabs/basic-android-kotlin-training-repository-pattern

https://medium.com/androiddevelopers/all-about-preferences-datastore-cc7995679334

 */
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.ToggleableStateKey
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.protsprog.highroad.MainActivity
import com.protsprog.highroad.glance.theme.HighroadWidgetGlanceColorScheme
import com.protsprog.highroad.nav.ToDoCase
import com.protsprog.highroad.todo.WorkTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext

private val layoutStep = 8.dp

class ToDoListGlanceWidget : GlanceAppWidget() {

    lateinit var todoRepo: ToDoGlanceRepository
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        todoRepo = WidgetToDoGlanceContainer(context).todoListRepo
        provideContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
//        val repository = remember { todoRepo }
//        val todoList = repository.getFullList().collectAsState(initial = listOf<WorkTask>())
//        Log.d("TEST_REPO", "repository: ${repository}")

        GlanceTheme(HighroadWidgetGlanceColorScheme.colors) {
            Column(
                modifier = GlanceModifier.fillMaxSize().padding(layoutStep * 2)
                    .appWidgetBackground().background(GlanceTheme.colors.onPrimary)
                    .cornerRadius(layoutStep * 2),
            ) {
                Text(
                    text = "Simple To-Do list", style = TextStyle(
                        color = GlanceTheme.colors.primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = GlanceModifier.wrapContentWidth()
                        .clickable(
                            onClick =
                            actionStartActivity<MainActivity>(
                                parameters = actionParametersOf(
                                    ActionParameters.Key<String>("insertDestination") to ToDoCase.route
                                )
                            )
                        ), maxLines = 1
                )
                Spacer(modifier = GlanceModifier.height(layoutStep * 2))
                Button(
                    modifier = GlanceModifier.fillMaxWidth(),
                    text = "Update list",
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GlanceTheme.colors.primaryContainer,
                        contentColor = GlanceTheme.colors.onPrimaryContainer
                    ),
                    style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight.Bold
                    ),
                    onClick = actionRunCallback<ToDoListUpdateActions>(),
                    maxLines = 1
                )
                Spacer(modifier = GlanceModifier.height(layoutStep * 2))
                ToDoList()
            }
        }
    }

    @Composable
    private fun ToDoList() {
        val prefs = currentState<Preferences>()
        val listSort: List<Int> = prefs[stringSetPreferencesKey("tasksort")]?.let {
            it.map {
                it.toInt()
            }.toList()
        } ?: emptyList<Int>()
        LazyColumn {
            items(items = listSort, itemId = { it.toLong() }) { sort ->
                CheckBoxItem(sort)
            }
        }
    }

    @Composable
    private fun CheckBoxItem(sort: Int) {
        val prefs = currentState<Preferences>()
        val text = prefs[stringPreferencesKey("text ${sort}")] ?: ""
        val closed = prefs[booleanPreferencesKey("closed ${sort}")] ?: false
        CheckBox(
            text = text,
            checked = closed,
            onCheckedChange = actionRunCallback<ToDoListCheckedActions>(
                actionParametersOf(
                    toggledSortKey to sort,
                ),
            ),
            modifier = GlanceModifier.padding(12.dp),
        )
    }
}

private val toggledSortKey = ActionParameters.Key<Int>("ToggledSortKey")

class ToDoListUpdateActions : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        withContext(Dispatchers.IO) {
            val todoRepo = WidgetToDoGlanceContainer(context).todoListRepo
            var setSort = mutableSetOf<String>()
            todoRepo.getFullList()
                .catch { e -> Log.d("ERROR_COLLECT", "${e.message}") }
                .collect { list ->
                    list.forEach { task ->
//                    Log.d("TEST_REPO", "todoList: ${task}")

                        setSort.add(task.sort.toString())

                        updateAppWidgetState(context, glanceId) { state ->
                            state[stringPreferencesKey("text ${task.sort}")] = task.text
                            state[booleanPreferencesKey("closed ${task.sort}")] = task.closed
                        }
                    }

                    updateAppWidgetState(context, glanceId) { state ->
                        state[stringSetPreferencesKey("tasksort")] = setSort.toSet()
                    }

                    ToDoListGlanceWidget().update(context, glanceId)
                }
//        тута жізні нєт
        }
    }
}

class ToDoListCheckedActions : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val toggledSort = requireNotNull(parameters[toggledSortKey]) {
            "Add $toggledSortKey parameter in the ActionParameters."
        }

        val checked = requireNotNull(parameters[ToggleableStateKey]) {
            "This action should only be called in response to toggleable events"
        }

        updateAppWidgetState(context, glanceId) { state ->
            state[booleanPreferencesKey("closed ${toggledSort}")] = checked
        }

        ToDoListGlanceWidget().update(context, glanceId)

        withContext(Dispatchers.IO) {
            val todoRepo = WidgetToDoGlanceContainer(context).todoListRepo
            todoRepo.persistTask(
                WorkTask(
                    sort = toggledSort,
                    closed = checked
                )
            )
        }
    }
}