package com.example.branch.domain

data class CurrencyRates(
    val baseCurrency: String = "USD",
    val timeStamp: Long = 0,
    val rates: Map<String, Double>,
    val disclaimer: String = ""
)

data class Currency(
    val code: String,
    val Country: String,
    var amount: Double = 0.0
){
    override fun equals(other: Any?): Boolean {
        val currency = other!! as Currency
        return currency.amount != this.amount && currency.code == this.code
    }
}

