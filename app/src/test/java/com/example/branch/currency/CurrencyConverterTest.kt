package com.example.branch.currency

import com.example.branch.domain.Currency
import com.example.branch.domain.CurrencyConverter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CurrencyConverterTest {

    private lateinit var sut : CurrencyConverter

    @Before
    fun setup(){
        sut = CurrencyConverter()
    }

    @Test
    fun `should convert currencies wrt exchange rates`() {
        val rates = hashMapOf<String, Double>()
        rates["USD"] = 1.0
        rates["INR"] = 82.0
        rates["JPY"] = 132.0

        sut.setCurrencyRate(rates)

        val usdCurrency = Currency("USD", "America")
        val inrCurrency = Currency("INR", "India")
        val jpyCurrency = Currency("JPY", "Japan")

        sut.setCurrencies(listOf(usdCurrency, inrCurrency, jpyCurrency))
        val list =  sut.convertTo("USD",100.0)

        Assert.assertEquals(list.size , 3)
        Assert.assertEquals(list[1].code, "INR")
        Assert.assertTrue(list[1].amount == 8200.0)
        Assert.assertEquals(list[2].code, "JPY")
        Assert.assertTrue(list[2].amount == 13200.0)
    }

    @Test
    fun `should return empty list when currency not set`() {
       val list =  sut.convertTo("USD",100.0)
        Assert.assertTrue(list.isEmpty())
    }

    @Test
    fun `should return old list when currency rates not set`() {
        val rates = hashMapOf<String, Double>()
        rates["USD"] = 1.0
        rates["INR"] = 82.0
        rates["JPY"] = 132.0

        val usdCurrency = Currency("USD", "America")
        val inrCurrency = Currency("INR", "India")
        val jpyCurrency = Currency("JPY", "Japan")

        sut.setCurrencies(listOf(usdCurrency, inrCurrency, jpyCurrency))
        val list =  sut.convertTo("USD",100.0)
        Assert.assertTrue(list[0].code == "USD")
        Assert.assertTrue(list[0].amount == 0.0)
    }
}