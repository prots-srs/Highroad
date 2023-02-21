package ua.com.biz_s.highroad.articles.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ua.com.biz_s.highroad.articles.database.HighroadDatabase
import ua.com.biz_s.highroad.articles.database.asDomainModel
import ua.com.biz_s.highroad.articles.domain.Article
import ua.com.biz_s.highroad.articles.repository.ArticlesRepository
import java.io.IOException

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val articlesRepository =
        ArticlesRepository(HighroadDatabase.getDatabase(application).articleDao())

    val articleList: LiveData<List<Article>> =
        Transformations.map(articlesRepository.articleList.asLiveData()) {
            it.asDomainModel()
        }

    val _articleItem = MutableLiveData<Article>()
    val articleItem: LiveData<Article>
        get() = _articleItem

    private val _title = MutableLiveData<String>("From DB")
    val title: LiveData<String>
        get() = _title

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
//        refreshDataFromRepository()
    }

    fun refreshListFromRepository() {
        viewModelScope.launch {
            try {
                articlesRepository.refreshNetwork()
                _title.value = articlesRepository.title
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                _eventNetworkError.value = true
            }
        }
    }

    fun refreshItemFromRepository(id: Int) {
        viewModelScope.launch {
            try {
                articlesRepository.refreshNetworkItem(id)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                _eventNetworkError.value = true
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun defineArticleItem(id: Int) {
        articleList.value?.forEach {
            if (it.id == id) {
                _articleItem.value = it
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArticleViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

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
}