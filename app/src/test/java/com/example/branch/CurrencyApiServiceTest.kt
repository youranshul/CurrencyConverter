package com.example.branch

import com.skydoves.sandwich.isSuccess
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.test.runTest
import org.junit.*


class CurrencyApiServiceTest {

    companion object{
        @AfterClass
        @JvmStatic
        fun tearDown(){
            MockServer.server.shutdown()
        }

        @BeforeClass
        @JvmStatic
        fun setup(){
            MockServer.server.dispatcher = MockNetworkModule.dispatcher
        }
    }


    @Test
    fun `should fetch all currency rates successfully`() = runTest {
        val response = MockNetworkModule.mockApiService.fetchCurrencyRates()
        Assert.assertEquals(true, response.isSuccess)
    }

    @Test
    fun `should fetch all currencies successfully`() = runTest {
            val response = MockNetworkModule.mockApiService.fetchAllCurrencies()
            Assert.assertEquals(true, response.isSuccess)
    }



    @Test
    fun `should throw error when app id is not passed`() = runTest {
        MockNetworkModule.shouldAddAppId = false
        val response = MockNetworkModule.mockApiService.fetchCurrencyRates()
        response.onError {
            Assert.assertEquals(401, this.response.code())
        }
    }
}