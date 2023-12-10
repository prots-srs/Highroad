package com.protsprog.highroad.articles

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

interface JobsRepository {
    fun fetchList(): Job
    fun refreshList(): Job
    fun fetchItem(id: Int): Job
    fun refreshItem(id: Int): Job
    suspend fun fetchRepoPermission(operation: AccessOperation)
    fun onClickSubmit(putModel: ArticlePutModel): Job
}

interface IndicateServiceWork {
    fun viewRefreshing(show: Boolean)
    fun viewServiceError(show: Boolean)
}

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repo: ArticlesRepository,
) : ViewModel(), JobsRepository, IndicateServiceWork {

    var commonUiState by mutableStateOf(ArticleUiState())
    var serviceUiState by mutableStateOf(ServiceUiState())

    var permissionUiState by mutableStateOf(PermissionsUiState())

    var articleList by mutableStateOf(emptyList<ArticleListModel>())
    var articleItem by mutableStateOf(ArticleItemModel())

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


//            listModel.filter { item ->
//                    (uiState.showOnlyPublished && item.publish) || !uiState.showOnlyPublished
//                }.sortedBy { item ->
//                    if (uiState.sortBySortAsc) item.sort else item.aid
//                }
//            }
        }
    }

    override fun refreshList() = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: refresh list")
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
    override fun fetchItem(id: Int) = viewModelScope.launch {
//        Log.d("TEST_FLOW", "vm: fetch item")
        if (id > 0) {
            repo.fetchItemDb(id)
            repo.articleItem.collect { itemModel -> articleItem = itemModel }
        } else {
            articleItem = ArticleItemModel()
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
            showOnlyPublished = true
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

    override fun onClickSubmit(putModel: ArticlePutModel) = viewModelScope.launch {
        putErrors = ArticlePutErrorsUiState()

        viewRefreshing(true)

        repo.putItem(putModel).collect { response ->
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
                    response.data?.let { response ->
                        if (response.errors == null) {

                            Log.d("TEST_FLOW", "vm put true: ${response.id}")
                        }
                        Log.d("TEST_FLOW", "vm put errors: ${response.errors}")
                        response.errors?.let { fullMap ->
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

    fun createMultipartBody(uri: Uri, multipartName: String): MultipartBody.Part {
        val documentImage = BitmapFactory.decodeFile(uri.path!!)
        val file = File(uri.path!!)
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        documentImage.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name = multipartName, file.name, requestBody)
    }

    override fun viewRefreshing(show: Boolean) {
        serviceUiState = serviceUiState.copy(hasRefreshing = show)
    }

    override fun viewServiceError(show: Boolean) {
        serviceUiState = serviceUiState.copy(hasServiceError = show)
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
