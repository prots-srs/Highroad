package com.protsprog.highroad.todo

data class WorkTask(
    val id: Int = 0,
    val sort: Int,
    var text: String = "",
    var closed: Boolean = false
)

data class TasksUIState(
    val editedTaskText: String = "",
    val editedTaskSort: Int = 0,
    val focusCaptured: Boolean = false
)