package com.protsprog.highroad.articles

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.protsprog.highroad.authentication.domen.AuthAppLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

interface JobsRepository {
    fun fetchList(): Job
    fun refreshList(): Job
    fun fetchItem(id: Int, fillPutModel: Boolean = false): Job
    fun refreshItem(id: Int): Job
    suspend fun fetchRepoPermission(operation: AccessOperation)
    fun onClickSubmit(): Job
    fun deleteItem(id: Int): Job
}

interface IndicateServiceWork {
    fun viewRefreshing(show: Boolean)
    fun viewServiceError(show: Boolean)
    fun enablingSubmitButton(enable: Boolean = true)
}

interface ServicesEditItem {
    fun changeInPutItemPublish(v: Boolean)
    fun changeInPutItemSort(v: String)
    fun changeInPutItemTitle(v: String)
    fun changeInPutItemDescription(v: String)
    fun changeInPutItemPicture(v: Uri)
    fun changeUseMedia(v: Boolean)
    fun changeUseCamera(v: Boolean)
}

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repo: ArticlesRepository,
) : ViewModel(), JobsRepository, IndicateServiceWork, ServicesEditItem {

    var commonUiState by mutableStateOf(ArticleUiState())
    var serviceUiState by mutableStateOf(ServiceUiState())

    var permissionUiState by mutableStateOf(PermissionsUiState())

    var articleList by mutableStateOf(emptyList<ArticleListModel>())
    var articleItem by mutableStateOf(ArticleItemModel())
    var articlePutItem by mutableStateOf(ArticlePutModel())

    var putErrors by mutableStateOf(ArticlePutErrorsUiState())

    override fun fetchList() = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: fetch list")
        repo.articleList.collect { listModel ->
            articleList = if (commonUiState.sortBySortAsc) {
                listModel.filter { item ->
                    (commonUiState.showOnlyPublished && item.publish) || !commonUiState.showOnlyPublished
                }.sortedBy { item ->
                    item.sort
                }
            } else {
                listModel.filter { item ->
                    (commonUiState.showOnlyPublished && item.publish) || !commonUiState.showOnlyPublished
                }.sortedByDescending { item ->
                    item.aid
                }
            }
        }
    }

    override fun refreshList() = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: refresh list")

//        serviceUiState = serviceUiState.copy(needRefresh = false)

        viewRefreshing(true)
        try {
            repo.refreshListNet()
            fetchPermisions()
//            Log.d("TEST_FLOW", "vm: service ok")
            viewServiceError(false)
        } catch (networkError: IOException) {
            viewServiceError(true)
//            Log.d("TEST_FLOW", "vm: service error ${networkError.message}")
        } finally {
            viewRefreshing(false)
        }
    }

    //    "fetch" - from Db
    override fun fetchItem(id: Int, fillPutModel: Boolean) = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: fetch item")
        if (id > 0) {
            repo.fetchItemDb(id)
            repo.articleItem.collect { itemModel ->
                if (fillPutModel) {
                    articlePutItem = itemModel.toPutModel()
                } else {
                    articleItem = itemModel
                }
            }
        } else {
            if (fillPutModel) {
                articlePutItem = ArticlePutModel()
            } else {
                articleItem = ArticleItemModel()
            }
        }
    }

    //    "refresh" - from net if imposable
    override fun refreshItem(id: Int) = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: refresh item")
        viewRefreshing(true)
        try {
            repo.refreshItemNet(id)
            fetchPermisions()

            viewServiceError(false)

            serviceUiState = serviceUiState.copy(needGoToDetailScreen = true)
        } catch (e: IOException) {
            viewServiceError(true)
        } finally {
            viewRefreshing(false)
        }
    }

    fun sortBySort(b: Boolean) {
        commonUiState = commonUiState.copy(sortBySortAsc = b)
        fetchList()
    }

    fun showOnlyPublished(b: Boolean) {
        commonUiState = commonUiState.copy(showOnlyPublished = b)
        fetchList()
    }

    fun fetchPermisions() {
        permissionUiState = permissionUiState.copy(
            update = false,
            delete = false
        )

        commonUiState = commonUiState.copy(
            sortBySortAsc = true,
            showOnlyPublished = false
        )

        if (AuthAppLogin.token != null) {
            viewModelScope.launch {
                fetchRepoPermission(AccessOperation.UPDATE)
            }
            viewModelScope.launch {
                fetchRepoPermission(AccessOperation.DELETE)
            }
        }
    }

    override suspend fun fetchRepoPermission(operation: AccessOperation) {
        repo.fetchAccess(operation).collect { access ->
            when (access) {
                is ResponseData.Loading -> {
                    viewRefreshing(access.status)
                    viewServiceError(false)
                }

                is ResponseData.Error -> {
                    viewRefreshing(false)
                    viewServiceError(true)
                }

                is ResponseData.Success -> when (operation) {
                    AccessOperation.UPDATE -> {
                        permissionUiState =
                            permissionUiState.copy(update = if (access.data != null) access.data.can else false)

                        viewRefreshing(false)
                        viewServiceError(false)
                    }

                    AccessOperation.DELETE -> {
                        permissionUiState =
                            permissionUiState.copy(delete = if (access.data != null) access.data.can else false)

                        viewRefreshing(false)
                        viewServiceError(false)
                    }
                }
            }
        }
    }

    fun breakPermissions() {
        permissionUiState = PermissionsUiState()
    }

    override fun onClickSubmit() = viewModelScope.launch {
        putErrors = ArticlePutErrorsUiState()

        viewRefreshing(true)

        if (!serviceUiState.hasChangePicture) {
            articlePutItem = articlePutItem.copy(picture = Uri.EMPTY)
        }

//        Log.d("TEST_FLOW", "vm putModel: ${putModel}")

        repo.putItem(articlePutItem).collect { response ->
            when (response) {
                is ResponseData.Loading -> {
                    viewRefreshing(response.status)
                    viewServiceError(false)
                    if (response.status) {
                        enablingSubmitButton(false)
                    }
                }

                is ResponseData.Error -> {
                    putErrors = putErrors.copy(common = response.error)

                    viewRefreshing(false)
                    viewServiceError(true)
                    enablingSubmitButton()
                }

                is ResponseData.Success -> {
                    response.data?.let { response ->
                        if (response.errors == null) {
                            articlePutItem = articlePutItem.copy(id = response.id)
//                            serviceUiState = serviceUiState.copy(idDetailScreen = response.id)
                            refreshItem(response.id)
//                            Log.d("TEST_FLOW", "vm put true: ${response.id}")
                        }
//                        Log.d("TEST_FLOW", "vm put errors: ${response.errors}")
                        response.errors?.let { fullMap ->
                            enablingSubmitButton()

                            fullMap.keys.forEach { key ->
                                fullMap[key]?.let { list ->
                                    putErrors = when (key) {
                                        "sort" -> putErrors.copy(
                                            sort = list.joinToString("\n")
                                        )

                                        "title" -> putErrors.copy(
                                            title = list.joinToString("\n")
                                        )

                                        "description" -> putErrors.copy(
                                            description = list.joinToString("\n")
                                        )

                                        else -> putErrors.copy(
                                            common = list.joinToString("\n")
                                        )
                                    }
                                }
                            }
                        }
                    }

                    viewRefreshing(false)
                    viewServiceError(false)
                }
            }
        }

    }

    override fun viewRefreshing(show: Boolean) {
        serviceUiState = serviceUiState.copy(hasRefreshing = show)
    }

    override fun viewServiceError(show: Boolean) {
        serviceUiState = serviceUiState.copy(hasServiceError = show)
    }

    override fun enablingSubmitButton(enable: Boolean) {
        serviceUiState = serviceUiState.copy(enableSubmit = enable)
    }

    override fun changeInPutItemPublish(v: Boolean) {
        articlePutItem = articlePutItem.copy(publish = v)
    }

    override fun changeInPutItemSort(v: String) {
        articlePutItem = articlePutItem.copy(sort = v)
    }

    override fun changeInPutItemTitle(v: String) {
        articlePutItem = articlePutItem.copy(title = v)
    }

    override fun changeInPutItemDescription(v: String) {
        articlePutItem = articlePutItem.copy(description = v)
    }

    override fun changeInPutItemPicture(v: Uri) {
        articlePutItem = articlePutItem.copy(picture = v)
        serviceUiState =
            if (v != Uri.EMPTY) serviceUiState.copy(hasChangePicture = true) else serviceUiState.copy(
                hasChangePicture = false
            )
    }

    override fun changeUseCamera(v: Boolean) {
        serviceUiState = serviceUiState.copy(useCamera = v)
    }

    override fun changeUseMedia(v: Boolean) {
        serviceUiState = serviceUiState.copy(useMedia = v)
    }

    override fun deleteItem(id: Int) = viewModelScope.launch {

        repo.deleteItem(id).collect { response ->
            when (response) {
                is ResponseData.Loading -> {
                    viewRefreshing(response.status)
                    viewServiceError(false)
                }

                is ResponseData.Error -> {
                    putErrors = putErrors.copy(common = response.error)

                    viewRefreshing(false)
                    viewServiceError(true)
                }

                is ResponseData.Success -> {
                    response.data?.let {
                        if (it.code == 1 && it.context == id) {
                            serviceUiState = serviceUiState.copy(needGoToListScreen = true)
                        } else {
                            putErrors = putErrors.copy(common = it.message)
                        }
                    }

                    viewRefreshing(false)
                    viewServiceError(false)
                }
            }
        }
    }
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
