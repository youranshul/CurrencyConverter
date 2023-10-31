package com.example.branch

import com.example.branch.data.CurrencyApiService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MockNetworkModule {

    var shouldAddAppId = true;

    private val requestInterceptor = Interceptor { chain ->
        val original = chain.request()
        val originalUrl = original.url

        val newUrlBuilder = originalUrl.newBuilder()

        if(shouldAddAppId) {
            newUrlBuilder.addQueryParameter("app_id", BuildConfig.API_KEY)
        }

        val originalBuilder = original.newBuilder().url(newUrlBuilder.build()).build()

        chain.proceed(originalBuilder)
    }

    internal val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
             if(request.requestUrl != null){
                 val query = request.requestUrl!!.queryParameterNames
                 if(query.contains("app_id").not()){
                    return MockResponse().setResponseCode(401).setBody("Client did not provide an App ID")
                 }
             }

            return when (request.path) {
                "/api/latest.json?app_id=${BuildConfig.API_KEY}" -> MockResponse().setResponseCode(200)
                    .setBody(LATEST_CURRENCY)
                "/api/currencies.json?app_id=${BuildConfig.API_KEY}" -> MockResponse().setResponseCode(200)
                    .setBody(CURRENCY_RATES)
                else -> {
                    MockResponse().setResponseCode(404)
                }
            }
        }
    }

    internal val mockApiService =
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .build()
            )
            .baseUrl(MockServer.server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
}