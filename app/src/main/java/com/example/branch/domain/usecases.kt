package com.example.branch.domain

import kotlinx.coroutines.flow.Flow

fun interface GetCurrenciesUseCase : suspend () -> Flow<List<Currency>>

fun interface GetCurrencyRatesUseCase : suspend () -> Flow<CurrencyRates>