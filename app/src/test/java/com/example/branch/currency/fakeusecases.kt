package com.example.branch.currency

import com.example.branch.data.CurrencyRepository
import com.example.branch.domain.Currency
import com.example.branch.domain.CurrencyRates
import com.example.branch.domain.GetCurrenciesUseCase
import com.example.branch.domain.GetCurrencyRatesUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

fun getFakeCurrencyUseCase(): GetCurrenciesUseCase {
    return GetCurrenciesUseCase {
        ::fetchCurrencies.invoke()
    }
}

fun getFakeCurrencyRateUseCase(): GetCurrencyRatesUseCase {
    return GetCurrencyRatesUseCase {
        ::fetchRates.invoke()
    }
}

suspend fun fetchRates() = flow {
    val rates = hashMapOf<String, Double>()
    rates["USD"] = 1.0
    rates["INR"] = 82.0
    rates["JPY"] = 132.0
    emit(CurrencyRates(rates = rates))
}

suspend fun fetchCurrencies() = flow {
    val usdCurrency = Currency("USD", "America")
    val inrCurrency = Currency("INR", "India")
    val jpyCurrency = Currency("JPY", "Japan")
    emit(listOf(usdCurrency, inrCurrency, jpyCurrency))
}