package com.example.branch.app

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.branch.data.CurrencyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import javax.inject.Inject

@HiltWorker
class ExchangeRateUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: CurrencyRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val currencies = repository.fetchCurrencies().single()
            val rates = repository.fetchCurrencyRates().single()
            val isRequestSuccess = currencies.isNotEmpty() && rates.rates.isNotEmpty()
            return if (isRequestSuccess) Result.success() else Result.failure()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}