package com.protsprog.highroad.glance

import android.content.Context
import android.util.Log
import com.protsprog.highroad.todo.ToDoListDao
import com.protsprog.highroad.todo.ToDoListDatabase
import com.protsprog.highroad.todo.ToDoRepo
import com.protsprog.highroad.todo.WorkTask
import com.protsprog.highroad.todo.asUIState
import com.protsprog.highroad.todo.di.ToDoDbModule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ToDoGlanceRepository(
    private val dao: ToDoListDao
) : ToDoRepo {
    override fun getFullList(): Flow<List<WorkTask>> = dao.getAll().map {
        it.map {
            it.asUIState()
        }
    }

    override suspend fun persistTask(task: WorkTask) {
        val daoTask = dao.getTask(task.sort)
        daoTask?.let {
            dao.update(
                daoTask.copy(
                    closed = task.closed
                )
            )
        }
    }

}

interface ToDoGlanceContainer {
    val todoListRepo: ToDoGlanceRepository
}

class WidgetToDoGlanceContainer(@ApplicationContext private val context: Context) :
    ToDoGlanceContainer {
    override val todoListRepo: ToDoGlanceRepository by lazy {
        ToDoGlanceRepository(ToDoDbModule.provideDatabase(context).todoDao())
    }
}