package com.example.branch.data

import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApiService {

    @GET("api/latest.json")
    suspend fun fetchCurrencyRates(): ApiResponse<CurrencyRatesResponse>

    @GET("api/currencies.json")
    suspend fun fetchAllCurrencies(): ApiResponse<Map<String, String>>
}