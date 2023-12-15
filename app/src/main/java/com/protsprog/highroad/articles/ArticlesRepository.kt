package com.protsprog.highroad.articles

/*
TO READ

https://developer.android.com/reference/android/content/ContentResolver#openInputStream(android.net.Uri)
https://developer.android.com/reference/android/provider/OpenableColumns

https://developer.android.com/training/data-storage/shared/media
https://developer.android.com/training/secure-file-sharing/retrieve-info

https://developer.android.com/privacy-and-security/risks/content-resolver

https://developer.android.com/guide/topics/providers/content-provider-basics

https://stackoverflow.com/questions/5657411/android-getting-a-file-uri-from-a-content-uri

https://medium.com/@njorogegwanjiru/picture-perfect-code-uploading-images-to-the-server-with-kotlin-and-jetpack-compose-using-a-restful-c1d30e1ff968
 */
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.annotation.WorkerThread
import com.protsprog.highroad.authentication.domen.AuthAppLogin
import dagger.hilt.android.qualifiers.ApplicationContext
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
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

sealed interface ResponseData<out T> {
    data class Success<T>(val data: T?) : ResponseData<T>
    data class Error(val error: String = "") : ResponseData<Nothing>
    data class Loading(val status: Boolean) : ResponseData<Nothing>
}

enum class AccessOperation { UPDATE, DELETE }

class ArticlesRepository @Inject constructor(
    private val articleDao: ArticleDao,
    @ApplicationContext private val appContext: Context
) {
    val articleList: Flow<List<ArticleListModel>> = articleDao.getAll().map {
//        Log.d("TEST_FLOW", "repo: fetch list")
        it.map(ArticleEntity::asModel)
    }.catch { emit(ArticleListModel.exampleItem()) }

    lateinit var articleItem: Flow<ArticleItemModel>

    suspend fun fetchItemDb(id: Int) = withContext(Dispatchers.IO) {
//        Log.d("TEST_FLOW", "repo: fetch item")
        articleItem =
            articleDao.getItemStream(id).map { it.asItemModel() }.catch { emit(ArticleItemModel()) }
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
//        Log.d("TEST_FLOW", "repo: refresh item ${item}")
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
//                    Log.d("TEST_FLOW", "repo itemPut: ${itemPut}")

                    val response = ServiceApi.articleService.putItem(
                        token = "Bearer ${AuthAppLogin.token}",
                        picture = createMultipartBody(itemPut.picture, multipartName = "picture"),
//                        requestBody = "picture".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
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
//                    Log.d("TEST_FLOW", "repo IO error: ${e.message}")
                    emit(ResponseData.Error("Network error: ${e.message.toString()}"))
                } catch (e: HttpException) {
//                    Log.d("TEST_FLOW", "repo HTTP error: ${e.message}")
                    emit(ResponseData.Error("http error: ${e.message.toString()}"))
                }
            }.onStart { emit(ResponseData.Loading(true)) }
                .onCompletion { emit(ResponseData.Loading(false)) }
                .catch { error -> emit(ResponseData.Error(error.message.toString())) }
        }

    suspend fun createMultipartBody(uri: Uri, multipartName: String): MultipartBody.Part {
        if (uri != Uri.EMPTY) {

            val fileType = appContext.contentResolver.getType(uri)
            val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
            var fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

            val crQuery = appContext.contentResolver.query(uri, null, null, null, null)
            crQuery?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()

                fileName = cursor.getString(nameIndex)

//                Log.d("TEST_FLOW", "repo file name: ${cursor.getString(nameIndex)}")
//                Log.d("TEST_FLOW", "repo file size: ${cursor.getLong(sizeIndex)}")
            }

            // Creating Temp file
            val tempFile = File(appContext.cacheDir, fileName)
            tempFile.createNewFile()

//            Log.d("TEST_FLOW", "repo file name: ${tempFile.name}")

            try {
                val oStream = FileOutputStream(tempFile)
                val inputStream = appContext.contentResolver.openInputStream(uri)

                inputStream?.let {
                    copy(inputStream, oStream)
                }

                oStream.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val requestFile = tempFile.asRequestBody(fileType?.toMediaTypeOrNull())

            return MultipartBody.Part.createFormData(
                name = multipartName,
                tempFile.name,
                requestFile
            )
        } else {
            return MultipartBody.Part.createFormData(name = multipartName, "")
        }
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    suspend fun deleteItem(id: Int): Flow<ResponseData<ArticleDeleteAnswer>> = withContext(Dispatchers.IO) {
//        Log.d("TEST_FLOW", "repo deleteItem id: ${id}")

        flow {
            try {
                val response = ServiceApi.articleService.deleteItem(
                    token = "Bearer ${AuthAppLogin.token}",
                    id = id
                )

                val answer = response.body()

                if (response.code() == 200 && answer is ArticleDeleteAnswer) {
                    if (answer.code == 1 && answer.context == id) {
                        val item = articleDao.getItem(id)
                        item?.let {
                            articleDao.delete(it)
                        }
                    }
                    emit(ResponseData.Success(answer))
                } else {
                    emit(ResponseData.Error("Token or request data wrong"))
                }
            } catch (e: IOException) {
//                    Log.d("TEST_FLOW", "repo IO error: ${e.message}")
                emit(ResponseData.Error("Network error: ${e.message.toString()}"))
            } catch (e: HttpException) {
//                    Log.d("TEST_FLOW", "repo HTTP error: ${e.message}")
                emit(ResponseData.Error("http error: ${e.message.toString()}"))
            }
        }.onStart { emit(ResponseData.Loading(true)) }
            .onCompletion { emit(ResponseData.Loading(false)) }
            .catch { error -> emit(ResponseData.Error(error.message.toString())) }

//        Log.d("TEST_FLOW", "repo deleteItem answer: ${answer}")
    }

}