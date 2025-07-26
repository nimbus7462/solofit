package com.example.solofit.DailySummaryActivities

import android.content.Intent
import com.example.solofit.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.DailySummaryPageBinding
import com.example.solofit.utilities.UserStreakManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailySummaryActivity: AppCompatActivity() {
    private lateinit var viewBinding: DailySummaryPageBinding
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DailySummaryPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val todaysUQAs = dbHelper.getUserQuestsByStatusAndDate(null, today)

        val completedUQAs = dbHelper.getUserQuestsByStatusAndDate("COMPLETED", today)
        val abortedUQAs = dbHelper.getUserQuestsByStatusAndDate("ABORTED", today)

        val completedQuests = completedUQAs.mapNotNull { dbHelper.getQuestById(it.questID) }
        val abortedQuests = abortedUQAs.mapNotNull { dbHelper.getQuestById(it.questID) }
/*
        val TAG = "DailySummaryDebug"

// Log UserQuestActivities
        Log.d(TAG, "---- Today's UQAs ----")
        todaysUQAs.forEach {
            Log.d(TAG, it.toString())
        }

        Log.d(TAG, "---- Completed UQAs ----")
        completedUQAs.forEach {
            Log.d(TAG, it.toString())
        }

        Log.d(TAG, "---- Aborted UQAs ----")
        abortedUQAs.forEach {
            Log.d(TAG, it.toString())
        }

// Log matching Quest objects
        Log.d(TAG, "---- Completed Quests ----")
        completedQuests.forEach {
            Log.d(TAG, it.toString())
        }

        Log.d(TAG, "---- Aborted Quests ----")
        abortedQuests.forEach {
            Log.d(TAG, it.toString())
        }
*/
        val statTotals = mutableMapOf("Strength" to 0, "Endurance" to 0, "Vitality" to 0)
        for (quest in completedQuests) {
            statTotals[quest.questType] = statTotals.getOrDefault(quest.questType, 0) + quest.statReward
        }

        val streakCount = UserStreakManager.getStreakCount(this)

        viewBinding.txvAbortedQuestsVal.text = abortedUQAs.count().toString()
        viewBinding.txvCompletedQuestsVal.text = completedUQAs.count().toString()


        viewBinding.txvCurrStreakValueSumm.text = streakCount.toString()

        val strGain = statTotals["Strength"] ?: 0
        val endGain = statTotals["Endurance"] ?: 0
        val vitGain = statTotals["Vitality"] ?: 0

        viewBinding.txvSummGainedStr.text = if (strGain != 0) "( +$strGain )" else ""
        viewBinding.txvSummGainedEnd.text = if (endGain != 0) "( +$endGain )" else ""
        viewBinding.txvSummGainedVit.text = if (vitGain != 0) "( +$vitGain )" else ""


        val base = if (streakCount >= 3) 0.02 else 0.0
        val stepBonus = (streakCount / 5) * 0.05

        val streakMultiplier = 1.0 + base + stepBonus
        val totalGainedExp = completedQuests.sumOf { it.xpReward }
        val totalExpPenalty = abortedQuests.sumOf { it.xpReward / 2 }
        val totalNetExpGained = (totalGainedExp * streakMultiplier) - totalExpPenalty

        viewBinding.txvGainedExpVal.text = totalGainedExp.toString()
        viewBinding.txvStreakVal.text = streakMultiplier.toString()
        viewBinding.txvPenaltyVal.text = totalExpPenalty.toString()
        viewBinding.txvFinalExpValue.text = "$totalNetExpGained EXP"
        viewBinding.txvDateToday.text = "$today :"


        viewBinding.btnQuestSummary.setOnClickListener {
            val intent = Intent(this, QuestSummaryActivity::class.java)
            intent.putParcelableArrayListExtra("uqaList", ArrayList(todaysUQAs))
            startActivity(intent)
        }

    }
}