package edu.ivytech.cnnreader

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsListViewModel() : ViewModel() {
    private val articleRepository = ArticleRepository.get()
    val articlesLiveData = articleRepository.getArticles()
}