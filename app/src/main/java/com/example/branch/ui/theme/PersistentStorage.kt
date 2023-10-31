package com.example.branch.ui.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.branch.domain.PersistentStorageService
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map

class PersistentStorage(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) :
    PersistentStorageService {

    private val currencykey = stringPreferencesKey("Currency")
    private val rateskey = stringPreferencesKey("rates")

    override suspend fun storeAllCurrencies(data: Map<String, String>) {
        dataStore.edit {
            val currencies = try {
                gson.toJson(data)
            } catch (e: Exception) {
                ""
            }
            it[currencykey] = currencies
        }
    }

    override suspend fun storeCurrencyRates(rates: Map<String, Double>) {
        dataStore.edit {
            val rates = try {
                gson.toJson(rates)
            } catch (e: Exception) {
                ""
            }
            it[rateskey] = rates
        }
    }

    override suspend fun getAllCurrencies(): Map<String, String> {
        return try {
            val currency = dataStore.data.first()[currencykey]
            return gson.fromJson(currency, HashMap<String, String>().javaClass)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    override suspend fun getCurrencyRates(): Map<String, Double> {
        return try {
            val rates = dataStore.data.first()[rateskey]
            return gson.fromJson(rates, HashMap<String, Double>().javaClass)
        } catch (e: Exception) {
            emptyMap()
        }
    }
}