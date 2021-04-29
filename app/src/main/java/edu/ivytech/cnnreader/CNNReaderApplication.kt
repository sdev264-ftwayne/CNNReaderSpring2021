package edu.ivytech.cnnreader

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs

const val NOTIFICATION_CHANNEL_ID = "cnn_reader"
class CNNReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ArticleRepository.initialize(this)
        val articleRepository = ArticleRepository.get()
        val executor = Executors.newSingleThreadExecutor()

        val feedInfo = articleRepository.getFeedInfo()
        if(feedInfo == null)
        {
            var feed = FeedInfo()
            articleRepository.addFeedInfo(feed)
        }

        val current = Date()
        val diffMillis : Long = abs(current.time - (feedInfo?.getPubDateLong() ?:0 ))
        if(TimeUnit.HOURS.convert(diffMillis, TimeUnit.MILLISECONDS) > 24 || feedInfo == null)
        {
            executor.execute {
                val newsFeedDownloader = NewsFeedDownloader(this)
                newsFeedDownloader.downloadFeed()
            }
        }

        val downloadWorkRequest : PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<DownloadWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("Download Feed", ExistingPeriodicWorkPolicy.KEEP, downloadWorkRequest)


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}