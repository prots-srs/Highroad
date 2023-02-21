package ua.com.biz_s.highroad

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import ua.com.biz_s.highroad.articles.database.HighroadDatabase
import ua.com.biz_s.highroad.articles.network.ArticlesApi
import ua.com.biz_s.highroad.articles.repository.ArticlesRepository
import ua.com.biz_s.highroad.articles.viewmodels.ArticleViewModel

@RunWith(AndroidJUnit4::class)
class ArticleViewModelTest {
    @Test
    fun addNewArticle_setsNewArticleEvent() {
        // Given a fresh ArticleViewModel
        val articleViewModel = ArticleViewModel(ApplicationProvider.getApplicationContext())


//        val articlesRepository = ArticlesRepository(HighroadDatabase.getDatabase(application))
//        val articleList = ArticlesApi.service.getList()
//        Log.d("NETWORK GET", articleList.toString())



        // When adding a new article


        // Then the new article event is triggered
    }
}