package com.example.solofit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.StatusAndQHistoryActivities.StatusActivity
import com.example.solofit.QuestboardActivities.QuestBoardActivity
import com.example.solofit.databinding.MainMenuBinding

class MainMenu : AppCompatActivity() {

    private lateinit var binding: MainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the ViewBinding
        binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button click now starts a new Activity instead of navigating fragments
        binding.btnManageQuests.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnQuestboard.setOnClickListener{
            val intent = Intent(this, QuestBoardActivity::class.java)
            startActivity(intent)
        }

        binding.btnStatus.setOnClickListener{
            val intent = Intent(this, StatusActivity::class.java)
            startActivity(intent)
        }

        binding.btnDailySummary.setOnClickListener{
            val intent = Intent(this, DailySummaryActivity::class.java)
            startActivity(intent)
        }

        binding.btnQuotes.setOnClickListener{
            val intent = Intent(this, QuotesActivity::class.java)
            startActivity(intent)
        }


    }
}
