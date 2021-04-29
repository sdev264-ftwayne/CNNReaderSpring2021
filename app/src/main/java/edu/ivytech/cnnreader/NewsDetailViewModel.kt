package edu.ivytech.cnnreader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class NewsDetailViewModel : ViewModel() {
    private val articleRepository = ArticleRepository.get()
    private val articleIdLiveData = MutableLiveData<UUID>()

    var articleLiveData : LiveData<Article> = Transformations.switchMap(articleIdLiveData) {
        id -> articleRepository.getArticle(id)
    }

    fun loadArticle(id:UUID) {
        articleIdLiveData.value = id
    }

}