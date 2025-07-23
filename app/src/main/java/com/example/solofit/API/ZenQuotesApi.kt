package com.example.solofit.API

import retrofit2.http.GET

interface ZenQuotesApi {
    @GET("api/quotes")
    suspend fun getQuotes(): List<ZenQuote>
}