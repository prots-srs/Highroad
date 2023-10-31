package com.protsprog.highroad.todo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
//    private val savedStateHandle: SavedStateHandle,
//    private val repo: ToDoRepositoryStatic
    private val repo: ToDoRepository
) : ViewModel() {

    var stateUI by mutableStateOf(TasksUIState())

    private val _tasks = mutableStateListOf<WorkTask>()
    val stateUITasks: List<WorkTask>
        get() = _tasks

    //auto
    /*val listState: StateFlow<List<WorkTask>> = repo.getFullList()
        .map {
            Log.d("TEXT_LIST", "stateIn: ${it}")
            it.map {
                it
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )
    companion object {
        private const val TIMEOUT_MILLIS = 3_000L
    }*/

    init {
        getPersistedTasks()
    }


    fun getPersistedTasks() {
        viewModelScope.launch {
            repo.getFullList().collect {
                _tasks.clear()
                _tasks.addAll(it)

                /*
                ????
                val finalList = _tasks
                    .plus(it)
                    .groupBy { task ->
                        task.id
                    }
                    .values
                    .map { valuesForId ->
                        valuesForId.maxByOrNull { it.sort }
                    }*/
            }
        }
    }

    fun setNewTask() {
        val sortIndex = getNewSortIndex()

        stateUI = stateUI.copy(
            editedTaskSort = sortIndex,
            editedTaskText = "",
            focusCaptured = false
        )
        viewModelScope.launch {
            val goalTask = getTaskBySort(sortIndex)
            repo.persistTask(goalTask)
            getPersistedTasks()
        }
    }

    fun captureFocus() {
        stateUI = stateUI.copy(
            focusCaptured = true
        )
    }

    fun onInputTaskEdit(input: String = "") {
        stateUI = stateUI.copy(
            editedTaskText = input
        )

        viewModelScope.launch {
            val goalTask = getTaskBySort(stateUI.editedTaskSort)
            goalTask.apply {
                text = input
            }
            repo.persistTask(goalTask)
            getPersistedTasks()
        }
    }

    fun onTextEndChange() {
        if (stateUI.focusCaptured) {
            stateUI = stateUI.copy(
                editedTaskText = "",
                editedTaskSort = 0,
                focusCaptured = false
            )
        }
    }

    fun onClickTaskEdit(sort: Int, text: String) {
        stateUI = stateUI.copy(
            editedTaskSort = sort,
            editedTaskText = text,
            focusCaptured = false
        )
    }

    fun changeClosed(sort: Int, checked: Boolean) {
        viewModelScope.launch {
            val goalTask = getTaskBySort(sort)
            goalTask.apply {
                closed = checked
            }
            repo.persistTask(goalTask)
            getPersistedTasks()
        }
    }

    fun deleteTask(task: WorkTask) {
        stateUI = stateUI.copy(
            editedTaskText = "",
            editedTaskSort = 0,
            focusCaptured = false
        )

        viewModelScope.launch {
            repo.removeTask(task)
            getPersistedTasks()
        }
    }

    private fun getNewSortIndex(): Int = if (_tasks.size == 0) {
        1
    } else {
        var start = 1
        _tasks.forEach {
            if (it.sort > start) {
                start = it.sort
            }
        }
        start.inc()
    }

    private fun getTaskBySort(sort: Int): WorkTask {
        val taskList = _tasks.filter { it.sort == sort }
        if (taskList != null && taskList.size > 0) {
            return taskList.first()
        } else {
            return WorkTask(sort = sort)
        }
    }
}