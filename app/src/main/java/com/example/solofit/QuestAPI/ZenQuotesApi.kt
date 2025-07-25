package com.example.solofit.QuestAPI

import retrofit2.http.GET

interface ZenQuotesApi {
    @GET("api/quotes")
    suspend fun getQuotes(): List<ZenQuote>
}