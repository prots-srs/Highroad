package ua.com.biz_s.highroad.articles.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ua.com.biz_s.highroad.articles.database.ArticleDao
import ua.com.biz_s.highroad.articles.database.DatabaseArticle
import ua.com.biz_s.highroad.articles.network.ArticlesApi
import ua.com.biz_s.highroad.articles.network.asDatabaseModel

class ArticlesRepository(private val dao: ArticleDao) {

    val articleList: Flow<List<DatabaseArticle>> = dao.getAll()

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun insert(items: List<DatabaseArticle>) {
//        dao.insertAll(items)
//    }

    private var _title: String? = null
    val title: String
        get() = _title ?: ""

    suspend fun refreshNetwork() {
        withContext(Dispatchers.IO) {
            val requestData = ArticlesApi.service.getList()
            _title = "abc"//requestData.title
            dao.insertAll(requestData.asDatabaseModel())
//            dao.insertAll(requestData)
        }
    }

    suspend fun refreshNetworkItem(id:Int) {
        withContext(Dispatchers.IO) {
            val requestData = ArticlesApi.service.getItem(id)
            dao.insertItem(requestData.asDatabaseModel())
        }
    }
}