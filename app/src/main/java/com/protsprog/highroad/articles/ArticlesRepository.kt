package com.protsprog.highroad.articles

import androidx.annotation.WorkerThread
import com.protsprog.highroad.data.local.database.ArticleDao
import com.protsprog.highroad.data.local.database.ArticleEntity
import com.protsprog.highroad.data.local.database.asExternalModel
import com.protsprog.highroad.data.model.ArticleAnonce
import kotlinx.coroutines.flow.Flow
import com.protsprog.highroad.data.network.ServiceApi
import com.protsprog.highroad.data.network.asEntity
import com.protsprog.highroad.data.network.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticlesRepository @Inject constructor(
    private val articleDao: ArticleDao
) {
    var articleList: Flow<List<ArticleAnonce>> = refreshFromLocal()

    fun refreshFromLocal() = articleDao.getAll()
        .map { it.map(ArticleEntity::asExternalModel) }
        .catch { emit(ArticleAnonce.empty()) }

    suspend fun refreshFromService() {
        withContext(Dispatchers.IO) {
            val requestData = ServiceApi.articleService.getList()
//            articleList = flow {
//                emit(requestData.map { item -> item.asExternalModel() })
//            }
            insertToDB(requestData.map { item -> item.asEntity() })
            articleList = refreshFromLocal()
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertToDB(items: List<ArticleEntity>) {
        articleDao.deleteAll()
        articleDao.insertAll(items)
    }
}