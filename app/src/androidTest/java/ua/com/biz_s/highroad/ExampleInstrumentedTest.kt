package ua.com.biz_s.highroad

import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import ua.com.biz_s.highroad.articles.viewmodels.ArticleViewModel

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ua.com.biz_s.highroad", appContext.packageName)
    }

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