package com.example.solofit.DailySummaryActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.DailySummaryPageBinding
import com.example.solofit.databinding.PopupDailyQuoteBinding
import com.example.solofit.databinding.PopupLegendBinding
import com.example.solofit.utilities.Extras
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailySummaryActivity: AppCompatActivity() {
    private lateinit var viewBinding: DailySummaryPageBinding
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    private var isSaved = false
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
        viewBinding.imbShowHideCalcInfo.setOnClickListener {
            val isShowing = viewBinding.imbShowHideCalcInfo.tag == "show"

            if (isShowing) {
                // Currently showing → Hide now
                viewBinding.imbShowHideCalcInfo.setImageResource(R.drawable.icon_hide)
                viewBinding.imbShowHideCalcInfo.tag = "hide"
                viewBinding.lloCalcInfo.visibility = View.GONE
            } else {
                // Currently hidden → Show now
                viewBinding.imbShowHideCalcInfo.setImageResource(R.drawable.icon_show)
                viewBinding.imbShowHideCalcInfo.tag = "show"
                viewBinding.lloCalcInfo.visibility = View.VISIBLE
            }
        }

        viewBinding.imbDailyQuote.setOnClickListener {
            showDailyQuotePopUp()
        }


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

    /*
    private fun showDailyQuotePopUp() {
        val popupBinding = PopupDailyQuoteBinding.inflate(layoutInflater)
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupBinding.root)

        popupBinding.txvQuoteDateToday.text = today
        popupBinding.imbSaveQuote.setOnClickListener {
            isSaved = !isSaved
            val updatedIcon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
            popupBinding.imbSaveQuote.setImageResource(updatedIcon)
            // TODO: Add quote logic here
            // chuck a random quote, must also have the save and unsave feature
        }
        popupBinding.btnGoBack.setOnClickListener {
            rootView.removeView(popupBinding.root)
        }
    }
     */
    private fun showDailyQuotePopUp() {
        val popupBinding = PopupDailyQuoteBinding.inflate(layoutInflater)
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupBinding.root)

        val quote = dbHelper.getRandomQuote()
        var isSaved = quote?.isSaved ?: false

        if (quote != null) {
            popupBinding.tvQuoteContent.text = "“${quote.quoteText}”"
            popupBinding.tvQuoteAuthor.text = "- ${quote.quoteAuthor}"
            popupBinding.imbSaveQuote.visibility = View.VISIBLE
            popupBinding.imbSaveQuote.setImageResource(
                if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
            )
        } else {
            popupBinding.tvQuoteContent.text = "No available quotes."
            popupBinding.tvQuoteAuthor.text = ""
            popupBinding.imbSaveQuote.visibility = View.GONE
        }

        popupBinding.txvQuoteDateToday.text = today

        popupBinding.imbSaveQuote.setOnClickListener {
            if (quote != null) {
                isSaved = !isSaved
                dbHelper.updateQuoteSaveStatus(quote.quoteID, isSaved)
                popupBinding.imbSaveQuote.setImageResource(
                    if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
                )
            }
        }

        popupBinding.btnGoBack.setOnClickListener {
            rootView.removeView(popupBinding.root)
        }
    }

}