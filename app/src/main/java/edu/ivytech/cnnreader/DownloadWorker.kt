package edu.ivytech.cnnreader

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

class DownloadWorker(val appContext: Context, workerParams : WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val articleRepository = ArticleRepository.get()
        val feedInfoOld = articleRepository.getFeedInfo()
        var nfd = NewsFeedDownloader(appContext)
        nfd.downloadFeed()
        val feedInfoNew = articleRepository.getFeedInfo()
        if(feedInfoNew?.getPubDateLong() ?: 1 != feedInfoOld?.getPubDateLong() ?: 0) {
            //notification
            val intent = MainActivity.newIntent(appContext)
            val pendingIntent = PendingIntent.getActivity(appContext,0,intent,0)
            val resources = appContext.resources
            val notification = NotificationCompat
                .Builder(appContext, NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.new_articles_title))
                .setSmallIcon(R.drawable.ic_baseline_new_releases_24)
                .setContentTitle(resources.getString(R.string.new_articles_title))
                .setContentText(resources.getString(R.string.new_articles_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            val notificationManager = NotificationManagerCompat.from(appContext)
            notificationManager.notify(0, notification)
            return Result.success()
        } else
            return Result.retry()

    }
}