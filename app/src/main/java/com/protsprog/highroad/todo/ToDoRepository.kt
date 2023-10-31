package com.protsprog.highroad.todo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ToDoRepo {
    fun getFullList(): Flow<List<WorkTask>>
    suspend fun persistTask(task: WorkTask)
    suspend fun removeTask(task: WorkTask) {}
}

class ToDoRepository @Inject constructor(
    private val dao: ToDoListDao
) : ToDoRepo {
    override fun getFullList(): Flow<List<WorkTask>> = dao.getAll().map {
        it.map {
            it.asUIState()
        }
    }

    override suspend fun persistTask(task: WorkTask) {
        withContext(Dispatchers.IO) {
            val daoTask = dao.getTask(task.sort)
            if (daoTask == null) {
                dao.insert(
                    ToDoEntity(
                        id = 0,
                        sort = task.sort,
                        text = task.text,
                        closed = task.closed
                    )
                )
            } else {
                dao.update(
                    daoTask.copy(
                        id = task.id,
                        text = task.text,
                        closed = task.closed
                    )
                )
            }
        }
    }

    override suspend fun removeTask(task: WorkTask) {
        withContext(Dispatchers.IO) {
            val daoTask = dao.getTask(task.sort)
            if (daoTask != null) {
                dao.delete(daoTask)
            }
        }
    }
}

class ToDoRepositoryStatic @Inject constructor() : ToDoRepo {
    //    temporal data store
    private val taskList = mutableListOf<WorkTask>()

    override fun getFullList() = flow {
        emit(taskList.toList())
    }

    override suspend fun persistTask(task: WorkTask) {
        if (taskList.filter { it.sort == task.sort }.size == 0) {
            taskList.add(task)
        } else {
            taskList.filter { it.sort == task.sort }?.first()?.apply {
                this.text = task.text
                this.closed = task.closed
            }
        }
    }

    override suspend fun removeTask(task: WorkTask) {
        taskList.remove(task)
    }
}