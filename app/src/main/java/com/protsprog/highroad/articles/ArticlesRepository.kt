package com.protsprog.highroad.articles

/*
TO READ

 */
import android.util.Log
import androidx.annotation.WorkerThread
import com.protsprog.highroad.authentication.domen.AuthAppLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

sealed interface ResponseData<out T> {
    data class Success<T>(val data: T?) : ResponseData<T>
    data class Error(val error: String = "") : ResponseData<Nothing>
    data class Loading(val status: Boolean) : ResponseData<Nothing>
}

enum class AccessOperation { UPDATE, DELETE }

class ArticlesRepository @Inject constructor(
    private val articleDao: ArticleDao
) {
    val articleList: Flow<List<ArticleListModel>> = articleDao.getAll().map {
//        Log.d("TEST_FLOW", "repo: fetch list")
        it.map(ArticleEntity::asModel)
    }.catch { emit(ArticleListModel.exampleItem()) }

    lateinit var articleItem: Flow<ArticleItemModel>

    suspend fun fetchItemDb(id: Int) = withContext(Dispatchers.IO) {
//        Log.d("TEST_FLOW", "repo: fetch item")
        articleItem =
            articleDao.getItem(id).map { it.asItemModel() }.catch { emit(ArticleItemModel()) }
    }

    @WorkerThread
    suspend fun refreshListNet() = withContext(Dispatchers.IO) {
//        Log.d("TEST_FLOW", "repo: refresh list")

//        var time = measureTimeMillis {
//        try {
        val list = ServiceApi.articleService.getList().map { item -> item.asEntity() }
        articleDao.deleteAll()
        articleDao.insertAll(list)
//        } catch (e: IOException) {
//            Throwable(e)
//            Log.d("TEST_FLOW", "repo: fetch net error: ${e.message}")
//        }
//        }
//        Log.d("TEST_SUSPEND", "delete: $time ms")

        /*
        val listDb = articleDao.getAll()
        listDb.collect {
            val timeDeleteItems = measureTimeMillis {
                it.forEach { entityDb ->
                    if (listNet.find { entityNet -> entityDb == entityNet } == null) {
                        articleDao.delete(entityDb)
                    }
                }
            }
            Log.d("TEST_SUSPEND", "delete items: $timeDeleteItems ms")
        }*/
    }

    @WorkerThread
    suspend fun refreshItemNet(id: Int) = withContext(Dispatchers.IO) {
//        Log.d("TEST_FLOW", "repo: refresh item")
//        try {
        val item = ServiceApi.articleService.getItem(id).asEntity()
        articleDao.upsertItem(item)
//        } catch (e: IOException) {
//            Log.d("TEST_FLOW", "refresh net: ${e.message}")
//        }
    }


    suspend fun fetchAccess(operation: AccessOperation): Flow<ResponseData<ArticleAccess>> =
        withContext(Dispatchers.IO) {
            flow {
                val operation = when (operation) {
                    AccessOperation.UPDATE -> "update"
                    AccessOperation.DELETE -> "delete"
                }
                try {
                    val response = ServiceApi.articleService.getAccess(
                        token = "Bearer ${AuthAppLogin.token}",
                        operation = operation
                    )

                    if (response.code() == 200 && response.body() is ArticleAccess) {
                        emit(ResponseData.Success(response.body()))
                    } else {
                        emit(ResponseData.Error("Token wrong"))
                    }
                } catch (e: IOException) {
                    emit(ResponseData.Error("Network error: ${e.message.toString()}"))
                }
            }.onStart { emit(ResponseData.Loading(true)) }
                .onCompletion { emit(ResponseData.Loading(false)) }
                .catch { error -> emit(ResponseData.Error(error.message.toString())) }
        }

    suspend fun putItem(
        itemPut: ArticlePutModel
    ): Flow<ResponseData<ArticlePutAnswer>> =
        withContext(Dispatchers.IO) {
            flow {
                try {
                    val response = ServiceApi.articleService.putItem(
                        token = "Bearer ${AuthAppLogin.token}",
                        image = itemPut.picture,
                        requestBody = "image".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        id = itemPut.id.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        publish = itemPut.publish.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        sort = itemPut.sort
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        title = itemPut.title
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        description = itemPut.description
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                    )

//                    Log.d("TEST_FLOW", "repo body: ${response.body()}")

                    if (response.code() == 200 && response.body() is ArticlePutAnswer) {
                        emit(ResponseData.Success(response.body()))
                    } else {
                        emit(ResponseData.Error("Token or request data wrong"))
                    }
                } catch (e: IOException) {
//                    Log.d("TEST_FLOW", "repo error: ${e.message}")
                    emit(ResponseData.Error("Network error: ${e.message.toString()}"))
                } catch (e: HttpException) {
                    emit(ResponseData.Error("http error: ${e.message.toString()}"))
                }
            }.onStart { emit(ResponseData.Loading(true)) }
                .onCompletion { emit(ResponseData.Loading(false)) }
                .catch { error -> emit(ResponseData.Error(error.message.toString())) }
        }

}