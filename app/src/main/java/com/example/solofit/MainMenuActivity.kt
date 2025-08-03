package com.example.solofit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.DailySummaryActivities.DailySummaryActivity
import com.example.solofit.ManageQuestFragments.NavHostManageQuest
import com.example.solofit.StatusAndQHistoryActivities.StatusActivity
import com.example.solofit.QuestboardActivities.QuestBoardActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.MainMenuBinding
import com.example.solofit.utilities.Extras
import androidx.core.net.toUri
import com.example.solofit.SettingsActivities.SettingsActivity
import com.example.solofit.QuoteActivities.QuoteActivity
import com.example.solofit.utilities.getColorForCategory
import com.example.solofit.utilities.getTitleColorCategory

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: MainMenuBinding
    private lateinit var dbHelper: MyDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = MyDatabaseHelper.getInstance(this)!!


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
            val intent = Intent(this, QuoteActivity::class.java)
            startActivity(intent)
        }

        binding.imbSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val currentUser = dbHelper.getUserById(Extras.DEFAULT_USER_ID)!! // Default User
        val displayName = if (currentUser.username.isNullOrBlank()) "Player" else currentUser.username
        binding.txvIgn.text = displayName
        if (currentUser.selectedTitle.isNullOrBlank()) {
            binding.txvUserTitle.visibility = View.GONE
        } else {
            binding.txvUserTitle.visibility = View.VISIBLE
            binding.txvUserTitle.text = currentUser.selectedTitle
            val category = getTitleColorCategory(currentUser.selectedTitle)
            val color = this.getColorForCategory(category)
            binding.txvUserTitle.setTextColor(color)
            binding.txvUserTitle.setShadowLayer(20f, 0f, 0f, R.color.black)
        }
        currentUser.pfpUri?.let {
            binding.imvPfp.setImageURI(it.toUri())
        }

    }

}
