package com.example.solofit.DailySummaryActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.DailySummaryPageBinding
import com.example.solofit.utilities.Extras
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

        val todaysUQAs = dbHelper.getUserQuestsByStatusDateAndUserID(null, today, Extras.DEFAULT_USER_ID)

        var user = dbHelper.getUserById(Extras.DEFAULT_USER_ID)
        val completedUQAs = dbHelper.getUserQuestsByStatusDateAndUserID("COMPLETED", today, Extras.DEFAULT_USER_ID)
        val abortedUQAs = dbHelper.getUserQuestsByStatusDateAndUserID("ABORTED", today, Extras.DEFAULT_USER_ID)

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

        val streakCount = user!!.streakCount

        viewBinding.txvAbortedQuestsVal.text = abortedUQAs.count().toString()
        viewBinding.txvCompletedQuestsVal.text = completedUQAs.count().toString()


        viewBinding.txvCurrStreakValueSumm.text = streakCount.toString()

        val strGain = statTotals["Strength"] ?: 0
        val endGain = statTotals["Endurance"] ?: 0
        val vitGain = statTotals["Vitality"] ?: 0

        viewBinding.txvSummStrValHere.text = user.strengthPts.toString()
        viewBinding.txvSummEndValHere.text = user.endurancePts.toString()
        viewBinding.txvSummVitValHere.text = user.vitalityPts.toString()
        viewBinding.txvSummGainedStr.text = if (strGain != 0) "( +$strGain From Today )" else ""
        viewBinding.txvSummGainedEnd.text = if (endGain != 0) "( +$endGain From Today )" else ""
        viewBinding.txvSummGainedVit.text = if (vitGain != 0) "( +$vitGain From Today )" else ""


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
        viewBinding.txvDateToday.text = "$today"


        viewBinding.btnQuestSummary.setOnClickListener {
            val intent = Intent(this, QuestSummaryActivity::class.java)
            intent.putParcelableArrayListExtra("uqaList", ArrayList(todaysUQAs))
            startActivity(intent)
        }

    }
}