package com.protsprog.highroad.articles

import androidx.lifecycle.*
import com.protsprog.highroad.data.model.ArticleAnonce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

data class ArticleListUiState(
    var isServiceError: Boolean = true,
    var isRefreshing: Boolean = false,
    var articleList: List<ArticleAnonce> = listOf()
)

/*
sealed interface ArticleListUiState {
    object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: List<String>) : MyModelUiState
}

 */

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticlesRepository,
) : ViewModel() {

    private val _listUiState = MutableStateFlow(ArticleListUiState())
    val listUiState: StateFlow<ArticleListUiState> = _listUiState.asStateFlow()

    init {
        viewModelScope.launch {
            articleRepository.articleList.collect {
                _listUiState.update { currentState ->
                    currentState.copy(
                        articleList = it
                    )
                }
            }
        }
    }

    fun refreshing() = viewModelScope.launch {
        _listUiState.update { currentState ->
            currentState.copy(
                isRefreshing = true
            )
        }
        refreshListFromRepository()
//        _listUiState.update { currentState ->
//            currentState.copy(
//                isRefreshing = false
//            )
//        }
    }

    suspend fun refreshListFromRepository() {
        try {
//            delay(1500)
            articleRepository.refreshFromService()

            articleRepository.articleList.collect {
                _listUiState.update { currentState ->
                    currentState.copy(
                        articleList = it,
                        isServiceError = false,
                        isRefreshing = false
                    )
                }
            }

        } catch (networkError: IOException) {
            _listUiState.update { currentState ->
                currentState.copy(
                    isServiceError = true,
                    isRefreshing = false
                )
            }
        }
    }

//class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    /*
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
    */
}