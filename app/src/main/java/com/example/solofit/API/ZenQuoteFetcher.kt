package com.example.solofit.api

import android.content.Context
import android.util.Log
import com.example.solofit.API.RetrofitClient
import com.example.solofit.API.ZenQuote
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ZenQuotesFetcher {

    suspend fun fetchAndInsertQuotesOnce(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val hasFetchedQuotes = prefs.getBoolean("quotes_fetched", false)

        if (hasFetchedQuotes) {
            Log.d("ZenQuotes", "Quotes already fetched. Skipping fetch.")
            return
        }

        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getQuotes()
            }

            if (!response.isNullOrEmpty()) {
                val dbHelper = MyDatabaseHelper.getInstance(context)!!
                val distinctQuotes = response.distinctBy { it.q to it.a }.take(10)

                for (quote in distinctQuotes) {
                    val quoteObj = Quote(
                        quoteText = quote.q,
                        quoteAuthor = quote.a
                    )
                    dbHelper.insertQuote(quoteObj)
                }

                prefs.edit().putBoolean("quotes_fetched", true).apply()
                Log.d("ZenQuotes", "Successfully inserted ${distinctQuotes.size} quotes.")
            } else {
                Log.w("ZenQuotes", "Response was empty.")
            }

        } catch (e: Exception) {
            Log.e("ZenQuotes", "Error fetching quotes: ${e.message}")
        }
    }
}
