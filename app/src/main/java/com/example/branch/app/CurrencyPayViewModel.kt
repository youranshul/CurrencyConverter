package com.example.branch.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branch.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyPayViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrenciesUseCase,
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val converter: CurrencyConverter,
    private val storageService: PersistentStorageService
) : ViewModel() {

    init {
        fetchCurrencies()
        fetchCurrencyRates()
    }

    private var conversionJob: Job? = null
    private var selectedCurrency: String = ""
    var amount = MutableStateFlow("")

    private val _currencies =
        MutableStateFlow<CurrencyScreenUiResult>(CurrencyScreenUiResult.Loading)
    val currencies = _currencies.asStateFlow()

    private fun fetchCurrencyRates() {
        viewModelScope.launch {
            getCurrencyRatesUseCase()
                .catch {
                    val storedData = handleCurrencyRateError()
                    emit(storedData)
                }
                .collect {
                    converter.setCurrencyRate(it.rates)
                }
        }
    }

    private suspend fun handleCurrencyRateError(): CurrencyRates {
        val storedRates = storageService.getCurrencyRates()
        return if (storedRates.isEmpty()) {
            CurrencyRates(rates = emptyMap())
        } else {
            CurrencyRates(rates = storedRates)
        }
    }

    private suspend fun handleCurrencyApiError(): List<Currency> {
        val storedCurrencies = storageService.getAllCurrencies()
        return if (storedCurrencies.isEmpty()) {
            emptyList()
        } else {
            mapToCurrency(storedCurrencies)
        }
    }

    private fun fetchCurrencies() {
        viewModelScope.launch {
            getCurrencyUseCase()
                .catch {
                    val storedDate = handleCurrencyApiError()
                    emit(storedDate)
                }
                .collect {
                    _currencies.value = CurrencyScreenUiResult.Success(it.toMutableList())
                    converter.setCurrencies(it)
                }
        }
    }

    fun onCurrencySelected(it: String) {
        conversionJob?.let {
            it.cancel()
        }
        selectedCurrency = it
        if (selectedCurrency.isEmpty()) return

        conversionJob = viewModelScope.launch(Dispatchers.Default) {
            val newAmount = amount.value
            if (newAmount.isNotEmpty()) {
                val modifiedList = converter.convertTo(selectedCurrency, newAmount.toDouble())
                _currencies.value = CurrencyScreenUiResult.Success(modifiedList)
            }
        }
    }

    fun onAmountChanged(it: String) {
        amount.value = it
        onCurrencySelected(selectedCurrency)
    }
}