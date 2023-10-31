package com.example.branch.data

import androidx.annotation.WorkerThread
import com.example.branch.domain.*
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: CurrencyApiService,
    private val storageService: PersistentStorageService
) {

    @WorkerThread
    suspend fun fetchCurrencies() = flow<List<Currency>> {
        api.fetchAllCurrencies().suspendOnSuccess {
            storageService.storeAllCurrencies(this.data)
            emit(mapToCurrency(this.data))
        }
    }.flowOn(Dispatchers.IO)


    @WorkerThread
    suspend fun fetchCurrencyRates() = flow<CurrencyRates> {
        api.fetchCurrencyRates().suspendOnSuccess {
            val currencyRates = this.data.mapToCurrencyRates()
            storageService.storeCurrencyRates(currencyRates.rates)
            emit(currencyRates)
        }
    }.flowOn(Dispatchers.IO)
}