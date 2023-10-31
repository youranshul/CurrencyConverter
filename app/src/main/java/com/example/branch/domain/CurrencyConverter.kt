package com.example.branch.domain

import androidx.annotation.VisibleForTesting
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt

class CurrencyConverter @Inject constructor() {

    private var currencyRates: Map<String, Double> = emptyMap()

    private var currencies: List<Currency> = emptyList()

    fun setCurrencyRate(rates: Map<String, Double>) {
        currencyRates = rates
    }

    fun setCurrencies(listOfCurrency: List<Currency>) {
        currencies = listOfCurrency
    }

    fun convertTo(code: String, amount: Double): MutableList<Currency> {
        val rate = currencyRates[code]
        rate?.let {
            val baseAmount = amount / rate
            currencyRates.forEach { (code, rate) ->
                currencies.find {
                    it.code == code
                }?.apply {
                    this.amount = (rate * baseAmount).roundTo(2)
                }
            }
        }
        return currencies.toMutableList()
    }

    private fun Double.roundTo(numFractionDigits: Int): Double {
        val factor = 10.0.pow(numFractionDigits.toDouble())
        return (this * factor).roundToInt() / factor
    }

}