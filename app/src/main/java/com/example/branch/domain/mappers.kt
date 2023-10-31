package com.example.branch.domain

import com.example.branch.data.CurrencyRatesResponse

fun CurrencyRatesResponse.mapToCurrencyRates(): CurrencyRates {
    return CurrencyRates(base, timestamp, rates, disclaimer)
}

fun mapToCurrency(currencyMap: Map<String, String>): List<Currency> {
    return currencyMap.map {
        Currency(it.key, it.value)
    }
}