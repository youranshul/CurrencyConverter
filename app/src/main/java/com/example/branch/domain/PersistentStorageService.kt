package com.example.branch.domain

interface PersistentStorageService {
    suspend fun storeAllCurrencies(data: Map<String, String>)
    suspend fun storeCurrencyRates(rates: Map<String, Double>)

    suspend fun getAllCurrencies():Map<String, String>
    suspend fun getCurrencyRates():Map<String, Double>
}