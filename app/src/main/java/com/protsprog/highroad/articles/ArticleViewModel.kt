package com.protsprog.highroad.articles

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

data class ArticleListModel(
    val aid: Int = 0,
    val sort: Int = 0,
    val title: String = "",
    val description: String? = null,
    val picture: String? = null
) {
    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
//    val shortDescription: String?
//        get() = description?.smartTruncate(200)
    companion object {
        fun exampleItem() = listOf(
            ArticleListModel(title = "Wrong loaded item")
        )
    }
}

data class ArticleItemModel(
    val id: Int = 0,
    val sort: Int = 0,
    val datetime: String = "",
    val title: String = "",
    val description: String? = null,
    val picture: String? = null
)

data class ArticleUiState(
    var isServiceError: Boolean = false,
    var isRefreshing: Boolean = false,
    var sorting: Boolean = true,
    var permissionEdit: Boolean = false
)

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repo: ArticlesRepository,
) : ViewModel() {

    var uiState by mutableStateOf(ArticleUiState())
    var articleList by mutableStateOf(emptyList<ArticleListModel>())
    var articleItem by mutableStateOf(ArticleItemModel())

    fun fetchList() = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: fetch list")
        repo.articleList.collect { listModel ->
            articleList = listModel.sortedBy {
                if (uiState.sorting) it.sort else it.aid
            }
        }
    }

    fun refreshList() = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: refresh list")
        uiState = uiState.copy(isRefreshing = true)
        uiState = try {
            repo.refreshListNet()
            uiState.copy(isServiceError = false, isRefreshing = false)
        } catch (networkError: IOException) {
            uiState.copy(isServiceError = true, isRefreshing = false)
        }
    }


    fun fetchItem(id: Int) = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: fetch item")
        repo.fetchItemDb(id)
        repo.articleItem.collect {
            articleItem = it
        }
    }

    fun sortBySort(b: Boolean) {
        uiState = uiState.copy(sorting = b)
        fetchList()
    }

    /*
    fun getNextId(curentId: Int): Int? {
        var nextId: Int? = null
        var next = false
        run loop@{
            articleList.value?.forEach {
                if (next) {
                    nextId = it.id
                    return@loop
                }
                if (it.id == curentId) {
                    next = true
                }
            }
        }
        return nextId
    }

    fun getPrevId(curentId: Int): Int? {
        var prevId: Int? = null
        run loop@{
            articleList.value?.forEach {
                if (it.id == curentId) {
                    return@loop
                }
                prevId = it.id
            }
        }
        return prevId
    }
    */
}