package com.example.solofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
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

        // Inflate the binding and set the root view
        binding = StartingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Log.d("StartingPage", "StartingPage launched")

        // creates the database as soon as the app is launched
        myDbHelper = MyDatabaseHelper.getInstance(this@StartingPageActivity)!!

        // 🔁 Fetch and insert ZenQuotes only once
        lifecycleScope.launch {
            ZenQuotesFetcher.fetchAndInsertQuotesOnce(applicationContext)

            // Fetch and log all quotes, Debugger
            val quotes = myDbHelper.getAllQuotes()
            for (quote in quotes) {
                Log.d("QUOTE_LOG", "\"${quote.quoteText}\" — ${quote.quoteAuthor}")
            }
        }

        // Button click listener
        binding.btnContinue.setOnClickListener {
            // If you're navigating to another activity, use Intent
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }
}
