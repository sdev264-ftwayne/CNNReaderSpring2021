package edu.ivytech.cnnreader

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class DownloadWorker(val appContext: Context, workerParams : WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val articleRepository = ArticleRepository.get()
        val feedInfoOld = articleRepository.getFeedInfo()
        var nfd = NewsFeedDownloader(appContext)
        nfd.downloadFeed()
        val feedInfoNew = articleRepository.getFeedInfo()

        if(feedInfoOld != null && feedInfoNew != null) {
            if(feedInfoNew.getPubDateLong() != feedInfoOld.getPubDateLong()) {
                //notification
                val intent = MainActivity.newIntent(appContext)
                val pendingIntent = PendingIntent.getActivity(appContext,0,intent,0)
                val resources = appContext.resources
                val notification = NotificationCompat
                    .Builder(appContext, NOTIFICATION_CHANNEL_ID)
                    .setTicker(resources.getString(R.string.new_articles_title))
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
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
        return Result.failure()
    }
}