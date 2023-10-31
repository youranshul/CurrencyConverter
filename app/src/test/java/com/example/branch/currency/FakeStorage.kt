package com.example.branch.currency

import com.example.branch.domain.PersistentStorageService

class FakeStorage : PersistentStorageService {

     private var currencyMap = mapOf<String, String>()
     private var currencyRateMap = emptyMap<String, Double>()

    override suspend fun storeAllCurrencies(currencies: Map<String, String>) {
        currencyMap = currencies
    }

    override suspend fun storeCurrencyRates(rates: Map<String, Double>) {
        currencyRateMap = rates
    }

    override suspend fun getAllCurrencies(): Map<String, String> {
        return currencyMap
    }

    override suspend fun getCurrencyRates(): Map<String, Double> {
        return currencyRateMap
    }
}
