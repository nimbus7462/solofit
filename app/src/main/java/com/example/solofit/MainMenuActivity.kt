package com.example.solofit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.DailySummaryActivities.DailySummaryActivity
import com.example.solofit.ManageQuestFragments.NavHostManageQuest
import com.example.solofit.StatusAndQHistoryActivities.StatusActivity
import com.example.solofit.QuestboardActivities.QuestBoardActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.MainMenuBinding
import com.example.solofit.model.User
import com.example.solofit.utilities.Extras
import androidx.core.net.toUri

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: MainMenuBinding
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the ViewBinding
        binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button click now starts a new Activity instead of navigating fragments
        binding.btnManageQuests.setOnClickListener {
            val intent = Intent(this, NavHostManageQuest::class.java)
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

        binding.imbSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
       // dbHelper.logAllUsers()
        val currentUser = dbHelper.getUserById(Extras.DEFAULT_USER_ID)!! // Default User
        val displayName = if (currentUser.username.isNullOrBlank()) "Player" else currentUser.username
        binding.txvIgn.text = displayName

        currentUser.pfpUri?.let {
            binding.imvPfp.setImageURI(it.toUri())
        }

    }

}
