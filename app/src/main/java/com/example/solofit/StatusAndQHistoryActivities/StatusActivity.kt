package com.example.solofit.StatusAndQHistoryActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.StatusPageBinding
import com.example.solofit.utilities.UserStreakManager

class StatusActivity: AppCompatActivity() {
    private lateinit var viewBinding: StatusPageBinding
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = StatusPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        val streakCount = UserStreakManager.getStreakCount(this)
        val streakRecord = UserStreakManager.getLongestStreak(this)

        viewBinding.txvCurrStreakValue.text = streakCount.toString()
        viewBinding.txvLongestStreakValue.text = streakRecord.toString()
        viewBinding.btnQuestHistory.setOnClickListener {
            val intent = Intent(this, QuestHistoryActivity::class.java)
            startActivity(intent)
        }

    }
}