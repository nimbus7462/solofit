package com.example.solofit.StatusAndQHistoryActivities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.StatusPageBinding
import com.example.solofit.model.User
import androidx.core.net.toUri
import com.example.solofit.R
import com.example.solofit.SettingsActivities.SettingsActivity
import com.example.solofit.utilities.Extras

class StatusActivity : AppCompatActivity() {
    private lateinit var viewBinding: StatusPageBinding
    private lateinit var user: User
    val dbHelper = MyDatabaseHelper.getInstance(this)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = StatusPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnQuestHistory.setOnClickListener {
            val intent = Intent(this, QuestHistoryActivity::class.java)
            startActivity(intent)
        }
        viewBinding.imbSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Re-fetch updated user data
        user = dbHelper.getUserById(Extras.DEFAULT_USER_ID)!!

        val streakCount = user.streakCount
        val streakRecord = user.longestStreak
        val unlockedTitles = user.unlockedTitles
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        android.util.Log.d("USER_TITLES", "Unlocked Titles (${unlockedTitles.size}): $unlockedTitles")
        viewBinding.txvLevelValue.text = user.level.toString()
        if (user.selectedTitle.isNullOrBlank()) {
            viewBinding.txvUserTitle.text = "NONE"
        } else {
            viewBinding.txvUserTitle.text = user.selectedTitle
        }
        viewBinding.progBarStatusLevel.progress = user.currentExp.toInt()
        viewBinding.progBarStatusLevel.max = user.expCap
        val expString = String.format("%.1f", user.currentExp)
        viewBinding.txvExpValues.text = "$expString / ${user.expCap}"
        // Update UI with latest data
        viewBinding.txvCurrStreakValue.text = streakCount.toString()
        viewBinding.txvLongestStreakValue.text = streakRecord.toString()

        viewBinding.txvStatusIgn.text = user.username

        user.pfpUri?.let {
            viewBinding.imvStatusPfp.setImageURI(it.toUri())
        }

        viewBinding.txvStrValHere.text = user.strengthPts.toString()
        viewBinding.txvEndValHere.text = user.endurancePts.toString()
        viewBinding.txvVitValHere.text = user.vitalityPts.toString()
    }
}
