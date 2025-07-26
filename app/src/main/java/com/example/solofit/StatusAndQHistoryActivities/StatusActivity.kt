package com.example.solofit.StatusAndQHistoryActivities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.StatusPageBinding
import com.example.solofit.model.User
import com.example.solofit.utilities.UserStreakManager
import androidx.core.net.toUri
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
    }

    override fun onResume() {
        super.onResume()

        // Re-fetch updated user data
        user = dbHelper.getUserById(Extras.DEFAULT_USER_ID)!!

        val streakCount = UserStreakManager.getStreakCount(this)
        val streakRecord = UserStreakManager.getLongestStreak(this)

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
