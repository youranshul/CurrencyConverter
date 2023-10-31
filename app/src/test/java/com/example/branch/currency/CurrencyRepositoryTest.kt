package com.example.branch.currency

import app.cash.turbine.test
import com.example.branch.CURRENCY_RATES
import com.example.branch.LATEST_CURRENCY
import com.example.branch.MockNetworkModule
import com.example.branch.MockServer
import com.example.branch.data.CurrencyRepository
import com.example.branch.domain.PersistentStorageService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CurrencyRepositoryTest {

    private lateinit var fakeStorage: PersistentStorageService
    private lateinit var sut: CurrencyRepository

    @Before
    fun setup() {
        fakeStorage = FakeStorage()
        sut = CurrencyRepository(MockNetworkModule.mockApiService, fakeStorage)
    }

    @Test
    fun `should fetch and store all the currencies`() = runTest {
        MockServer.server.enqueue(MockResponse().setResponseCode(200).setBody(LATEST_CURRENCY))
        sut.fetchCurrencies().test {
            val data = awaitItem()
            println(data)
            Assert.assertTrue(data.isNotEmpty())
            Assert.assertTrue(data[0].code.isNotEmpty())
            Assert.assertTrue(fakeStorage.getAllCurrencies().isNotEmpty())
            awaitComplete()
        }
    }

    @Test(expected = Throwable::class)
    fun `should throw server error when fetching currencies`() = runTest {
        MockServer.server.enqueue(MockResponse().setResponseCode(401))
        sut.fetchCurrencies().test {
            awaitError()
        }
    }

    @Test
    fun `should fetch and store all the currencies rates`() = runTest {
        MockServer.server.enqueue(MockResponse().setResponseCode(200).setBody(CURRENCY_RATES))
        sut.fetchCurrencyRates().test {
            val data = awaitItem()
            Assert.assertTrue(data.rates.isNotEmpty())
            Assert.assertTrue(data.baseCurrency.isNotEmpty())
            Assert.assertTrue(fakeStorage.getCurrencyRates().isNotEmpty())
            awaitComplete()
        }
    }

    @Test(expected = Throwable::class)
    fun `should throw server error when fetching rates`() = runTest {
        MockServer.server.enqueue(MockResponse().setResponseCode(401))
        sut.fetchCurrencyRates().test {
            awaitError()
        }
    }
}