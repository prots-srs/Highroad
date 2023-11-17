package com.protsprog.highroad.articles

/*
TO READ

 */
import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.system.measureTimeMillis

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
        try {
            val list = ServiceApi.articleService.getList().map { item -> item.asEntity() }
            articleDao.deleteAll()
            articleDao.insertAll(list)
        } catch(e: IOException) {
//            Log.d("TEST_FLOW", "fetch net: ${e.message}")
        }
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
}