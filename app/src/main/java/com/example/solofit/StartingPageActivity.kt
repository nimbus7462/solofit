package com.example.solofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.StartingPageBinding
import com.example.solofit.api.ZenQuotesFetcher
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class StartingPageActivity : AppCompatActivity() {

    private lateinit var binding: StartingPageBinding
    private lateinit var myDbHelper: MyDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StartingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("StartingPage", "StartingPage launched")

        myDbHelper = MyDatabaseHelper.getInstance(this@StartingPageActivity)!!

        lifecycleScope.launch {
            ZenQuotesFetcher.fetchAndInsertQuotesOnce(applicationContext)

            val quotes = myDbHelper.getAllQuotes()
            for (quote in quotes) {
                Log.d("QUOTE_LOG", "\"${quote.quoteText}\" — ${quote.quoteAuthor}")
            }
        }

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }
}
