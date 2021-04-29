package edu.ivytech.cnnreader

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs

const val NOTIFICATION_CHANNEL_ID = "cnn_reader"
class CNNReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ArticleRepository.initialize(this)
        val initialDownloadWorker = OneTimeWorkRequest.from(DownloadWorker::class.java)
        WorkManager.getInstance(this).enqueue(initialDownloadWorker)

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