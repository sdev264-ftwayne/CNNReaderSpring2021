package edu.ivytech.cnnreader

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.room.Room
import edu.ivytech.cnnreader.database.NewsDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "news_database"
class ArticleRepository private constructor(context: Context) {
    private val database:NewsDatabase = Room.databaseBuilder(context.applicationContext,
        NewsDatabase::class.java,
        DATABASE_NAME).build()
    private val newsDao = database.newsDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getArticles():LiveData<List<Article>> = newsDao.getArticles()
    fun getArticle(id : UUID) : LiveData<Article> = newsDao.getArticle(id)

    suspend fun getFeedInfo():FeedInfo? {
        var feedInfo : FeedInfo? = null
        newsDao.getFeedInfo().also { feedInfo = it }
        return feedInfo
    }
    fun deleteArticles() = newsDao.deleteArticles()
    fun updateFeed(feedInfo: FeedInfo) {
        executor.execute {
            newsDao.updateFeedInfo(feedInfo)
        }
    }
    fun addFeedInfo(feedInfo: FeedInfo) {
        executor.execute {
            try {
                newsDao.addFeedInfo(feedInfo)
            } catch (e: SQLiteConstraintException) {
                updateFeed(feedInfo)
            }
        }
    }
    fun addArticle(article: Article) {
        executor.execute {
            newsDao.addArticle(article)
        }
    }


    companion object {
        private var INSTANCE : ArticleRepository? = null
        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = ArticleRepository(context)
            }
        }

        fun get() : ArticleRepository {
            return INSTANCE?: throw IllegalStateException("Article Repository must be initialized")
        }
    }

}