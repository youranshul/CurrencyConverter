package com.example.branch.currency

import app.cash.turbine.test
import com.example.branch.MainCoroutineRule
import com.example.branch.app.CurrencyPayViewModel
import com.example.branch.app.CurrencyScreenUiResult
import com.example.branch.domain.CurrencyConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyPayViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var sut: CurrencyPayViewModel

    @Before
    fun setUp() {
        sut = CurrencyPayViewModel(
            getFakeCurrencyUseCase(),
            getFakeCurrencyRateUseCase(),
            CurrencyConverter(),
            FakeStorage()
        )
    }

    @Test
    fun `should have all the currencies when view model init`() = runTest() {
        val state = sut.currencies.value
        Assert.assertTrue(state is CurrencyScreenUiResult.Success)
        Assert.assertTrue((state as CurrencyScreenUiResult.Success).currency.isNotEmpty())
    }

    @Test
    fun `should get all currencies converted for 100 when from currency selected is USD`() =
        runTest() {
            sut.onCurrencySelected("USD")
            sut.onAmountChanged("100")
            val modifiedList = (sut.currencies.value as CurrencyScreenUiResult.Success).currency
            Assert.assertTrue(modifiedList.isNotEmpty())
            Assert.assertEquals(8200.00, modifiedList[1].amount, 8200.0)
        }

    @Test
    fun `should not convert when amount is not entered`() =
        runTest() {
            sut.onCurrencySelected("USD")
            val modifiedList = (sut.currencies.value as CurrencyScreenUiResult.Success).currency
            Assert.assertTrue(modifiedList.isNotEmpty())
            Assert.assertEquals(0.0, modifiedList[1].amount, 0.0)
        }

    @Test
    fun `should not convert when from currency selected`() =
        runTest() {
            sut.onAmountChanged("100")
            val modifiedList = (sut.currencies.value as CurrencyScreenUiResult.Success).currency
            Assert.assertTrue(modifiedList.isNotEmpty())
            Assert.assertEquals(0.0, modifiedList[1].amount, 0.0)
        }


}