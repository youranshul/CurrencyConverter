package com.example.branch.data

data class CurrencyRatesResponse(
    val base: String = "",
    val disclaimer: String = "",
    val license: String = "",
    val rates: Map<String, Double> = emptyMap(),
    val timestamp: Long = 0
)
