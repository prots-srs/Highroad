package com.protsprog.highroad.todo

/*
TO READ

https://developer.android.com/jetpack/compose/libraries
https://developer.android.com/jetpack/compose/compositionlocal

one example
https://medium.com/@kenruizinoue/intermediate-android-compose-todo-app-ui-1d808ef7882d
 */
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.protsprog.highroad.R
import com.protsprog.highroad.nav.ToDoCase
import com.protsprog.highroad.todo.ui.components.ToDoCard
import com.protsprog.highroad.ui.components.AppBar
import kotlinx.coroutines.launch

@Composable
fun ToDoScreen(
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Unit = {},
    viewModel: ToDoListViewModel = hiltViewModel()
) {

//    val taskList: List<WorkTask> by viewModel.listState.collectAsState() //.collectAsStateWithLifecycle()
//    Log.d("TEXT_LIST", "list: ${taskList}")

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                title = stringResource(id = ToDoCase.titleRes),
                onBackPressed = onNavigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.setNewTask()
                    if (viewModel.stateUITasks.lastIndex > 0) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(viewModel.stateUITasks.lastIndex)
                        }
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
//        Log.d("TEXT_LIST", "tasks screen: ${viewModel.stateUITasks.toList()}")
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            state = lazyListState
        ) {
            items(viewModel.stateUITasks, key = { it.sort }) { task ->
                ToDoCard(
                    stateTaskText = task.text,
                    stateTaskClosed = task.closed,
                    stateTaskEditing = task.sort == viewModel.stateUI.editedTaskSort,
                    onTextChange = viewModel::onInputTaskEdit,
                    onTextEndChange = viewModel::onTextEndChange,
                    captureFocus = viewModel::captureFocus,
                    onClickTaskEdit = {
                        viewModel.onClickTaskEdit(task.sort, task.text)
                    },
                    onClickCheckbox = { checked ->
                        viewModel.changeClosed(task.sort, checked)
                    },
                    deleteTask = { viewModel.deleteTask(task) }
                )
            }
        }
    }
}
