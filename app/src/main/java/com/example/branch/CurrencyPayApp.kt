package com.example.branch

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.branch.app.ExchangeRateUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CurrencyPayApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }


    override fun onCreate() {
        super.onCreate()
        scheduleWorker()
    }

    private fun scheduleWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val exchangeUpdateWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ExchangeRateUpdateWorker>(
                30,
                TimeUnit.MINUTES
            ).setConstraints(constraints)
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "exchangeRateUpdater",
                ExistingPeriodicWorkPolicy.KEEP,
                exchangeUpdateWorkRequest
            )
    }
}