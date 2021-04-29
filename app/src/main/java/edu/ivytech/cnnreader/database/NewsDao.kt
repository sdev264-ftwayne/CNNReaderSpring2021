package edu.ivytech.cnnreader.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.ivytech.cnnreader.Article
import edu.ivytech.cnnreader.FeedInfo
import java.util.*

@Dao
interface NewsDao {
    @Query("select * from article")
    fun getArticles(): LiveData<List<Article>>

    @Query("select * from article where id = (:id)")
    fun getArticle(id : UUID) : LiveData<Article>

    @Insert
    fun addArticle(article: Article)

    @Query("delete from article")
    fun deleteArticles()

    @Update
    fun updateFeedInfo(feedInfo: FeedInfo)

    @Query("select * from feedinfo where id = 1")
    fun getFeedInfo():FeedInfo?

    @Insert
    fun addFeedInfo(feedInfo: FeedInfo)
}