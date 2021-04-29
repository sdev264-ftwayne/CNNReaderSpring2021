package edu.ivytech.cnnreader

import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DownloadWorkerTest {
    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodicWork() {
        val context = InstrumentationRegistry.getTargetContext()
        val request = PeriodicWorkRequestBuilder<DownloadWorker>(1, TimeUnit.DAYS).build()
        val workManager = WorkManager.getInstance(context)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        workManager.enqueueUniquePeriodicWork("download Test",ExistingPeriodicWorkPolicy.KEEP, request)
        if (testDriver != null) {
            testDriver.setPeriodDelayMet(request.id)
        }
        val workInfo = workManager.getWorkInfoById(request.id).get()

        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }
}