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
import com.example.solofit.model.UserQuestActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val lastDateKey = "daily_quest_date_key" // or use getString(R.string.daily_quest_date_key)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = dateFormat.format(calendar.time)
        val lastDate = prefs.getString(lastDateKey, null)
        val allQuests = myDbHelper.getAllQuests()

        // If DB was just created → save today as last date (just for safety)
        if (allQuests.isEmpty()) {
            prefs.edit().putString(lastDateKey, todayDate).apply()
            Log.d("StartingPage", "DB is empty — save today's date as baseline")
        }

        // Pick & store daily quests only if new day
        if (lastDate == null || lastDate != todayDate) {
            val randomQuests = allQuests.shuffled().take(5)

            for (quest in randomQuests) {
                val uqa = UserQuestActivity(
                    questStatus = "CREATED",
                    userLogs = "",
                    dateCompleted = todayDate,
                    questID = quest.id!!,
                    quoteID = 0,
                    userID = 0
                )
                myDbHelper.insertUserQuestActivity(uqa)
            }

            prefs.edit().putString(lastDateKey, todayDate).apply()
            Log.d("StartingPage", "New daily quests generated & saved for $todayDate")
        } else {
            Log.d("StartingPage", "Same day — using existing daily quests in DB")
        }
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
