package edu.ivytech.cnnreader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ivytech.cnnreader.Article
import edu.ivytech.cnnreader.FeedInfo

@Database(entities= [Article::class, FeedInfo::class], version = 1)
@TypeConverters(NewsTypeConverters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao() : NewsDao
}