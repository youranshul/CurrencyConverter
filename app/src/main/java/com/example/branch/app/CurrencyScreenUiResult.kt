package com.example.branch.app

import com.example.branch.domain.Currency

sealed class CurrencyScreenUiResult {

    object Loading : CurrencyScreenUiResult()

    data class Success(val currency: List<Currency>) : CurrencyScreenUiResult()
}