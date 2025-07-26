package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.AbortCompleteQuestBinding
import com.example.solofit.model.Quest
import com.example.solofit.model.Quote
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras
import com.example.solofit.utilities.UserStreakManager
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.OnBackPressedCallback
import com.example.solofit.model.User

class QuestAbortCompleteActivity : AppCompatActivity() {

    private lateinit var viewBinding: AbortCompleteQuestBinding
    private var isSaved = false


    // Store the selected quote for use when saving to UQA
    private var selectedQuote: Quote? = null

    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    private lateinit var todaysSelectedUQA: UserQuestActivity
    private var questStatus: String? = null
    private lateinit var quest: Quest
    private lateinit var user: User
    private var hasSavedStatus = false // Track if save was already done
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = AbortCompleteQuestBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // Load quote
        loadAndDisplayQuote()
        Log.d("QUOTE_DEBUG", dbHelper.getAllQuotes().toString())

        val uqa = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                intent.getParcelableExtra(Extras.EXTRA_UQA, UserQuestActivity::class.java)
            else ->
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Extras.EXTRA_UQA)
        } ?: run {
            finish()
            return
        }

        // Get quest info from intent
        questStatus = intent.getStringExtra(Extras.EXTRA_QUEST_STATUS)


        if (uqa.questID == -1 || questStatus == null) {
            finish() // invalid id, exit early
            return
        } else {
            this.todaysSelectedUQA = uqa
        }

        user = dbHelper.getUserById(todaysSelectedUQA.userID) ?: run {
            finish()
            return
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveQuestStatusAndQuote(todaysSelectedUQA)
                setResult(RESULT_OK)
                finish()
            }
        })



        quest = dbHelper.getQuestById(todaysSelectedUQA.questID) ?: run {
            finish()
            return
        }

        // Quest name shown at the top
        viewBinding.txvQStatusQuestName.text = quest.questName

        viewBinding.txvTotalUserExp.text = user.currentExp.toString()
        viewBinding.txvStrValHere.text = user.strengthPts.toString()
        viewBinding.txvEndValHere.text = user.endurancePts.toString()
        viewBinding.txvVitValHere.text = user.vitalityPts.toString()


        // Display completion or abortion visuals
        when (questStatus) {
            Extras.STATUS_COMPLETED -> {
                viewBinding.lloQuestHeader.setBackgroundResource(R.drawable.bg_quest_complete)
                viewBinding.txvQuestStatusTitle.text = getString(R.string.quest_completed)
                viewBinding.btnFinishQuest.setBackgroundResource(R.drawable.bg_complete_btn)
                viewBinding.btnFinishQuest.text = getString(R.string.complete_quest)
                viewBinding.btnFinishQuest.setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_green))

                viewBinding.txvExpChanges.text = "( +${quest.xpReward} ) EXP"
                viewBinding.txvExpChanges.setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_green))
                viewBinding.txvExpChanges.setTextColor(getColor(R.color.bright_green))
                viewBinding.imvExpIcon.setImageResource(R.drawable.icon_exp_gain)

                val statTextView = when (quest.questType) {
                    "Strength" -> viewBinding.txvGainedStr
                    "Endurance" -> viewBinding.txvGainedEnd
                    "Vitality" -> viewBinding.txvGainedVit
                    else -> null
                }

                statTextView?.apply {
                    text = " ( +${quest.statReward} )"
                    setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_green))
                }
            }

            Extras.STATUS_ABORTED -> {
                viewBinding.lloQuestHeader.setBackgroundResource(R.drawable.bg_quest_abort)
                viewBinding.txvQuestStatusTitle.text = getString(R.string.quest_aborted)
                viewBinding.btnFinishQuest.setBackgroundResource(R.drawable.bg_return_btn)
                viewBinding.btnFinishQuest.text = getString(R.string.return_quest)
                viewBinding.btnFinishQuest.setShadowLayer(10f, 0f, 0f, getColor(R.color.light_gray))

                val penaltyExp = quest.xpReward / 2
                viewBinding.txvExpChanges.text = "( -$penaltyExp ) EXP"
                viewBinding.txvExpChanges.setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_red))
                viewBinding.imvExpIcon.setImageResource(R.drawable.icon_exp_loss)
            }
        }

        viewBinding.imbSaveQuote.setOnClickListener {
            isSaved = !isSaved
            val updatedIcon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
            viewBinding.imbSaveQuote.setImageResource(updatedIcon)

            selectedQuote?.let { quote ->
                dbHelper.updateQuoteSaveStatus(quote.quoteID, isSaved)
            }
        }

        // Log thoughts button
        viewBinding.btnLogThoughts.setOnClickListener {
            val intent = Intent(applicationContext, QuestLoggingActivity::class.java)
            saveQuestStatusAndQuote(todaysSelectedUQA) // Save on manual click
            setResult(RESULT_OK) // Let QuestBoardActivity know to refresh
            intent.putExtra(Extras.EXTRA_UQA, todaysSelectedUQA)
            startActivity(intent)
            finish()
        }

        // Finish Quest button — Save status + quote
        viewBinding.btnFinishQuest.setOnClickListener {
            saveQuestStatusAndQuote(todaysSelectedUQA) // Save on manual click
            setResult(RESULT_OK) // Let QuestBoardActivity know to refresh
            finish()
        }
    }

    // Load a random quote and update UI
    private fun loadAndDisplayQuote(){
        val quote = dbHelper.getRandomQuote()
        if (quote == null) {
            Log.d("QUOTE_DEBUG", "No quote was retrieved from the database.")
        } else {
            Log.d("QUOTE_DEBUG", "Retrieved quote: ID=${quote.quoteID}, Text=${quote.quoteText}") }
        quote?.let {
            selectedQuote = it

            viewBinding.txvQuoteContent.text = "“${it.quoteText}”"
            viewBinding.txvQuoteAuthor.text = "- ${it.quoteAuthor}"

        }
    }

    // Reusable method that updates the quest status and saves the quote ID into the UQA
    private fun saveQuestStatusAndQuote(userQuestActivity: UserQuestActivity) {
        if (hasSavedStatus) return // Don't save multiple times
        hasSavedStatus = true

        if (questStatus != null) {
            val newStatus = when (questStatus) {
                 Extras.STATUS_COMPLETED -> "COMPLETED"
                 Extras.STATUS_ABORTED -> "ABORTED"
                else -> userQuestActivity.questStatus
            }

            val updatedUQA = UserQuestActivity(
                userQuestActID = userQuestActivity.userQuestActID,
                questStatus = newStatus,
                userLogs = userQuestActivity.userLogs,
                dateCreated = userQuestActivity.dateCreated,
                questID = userQuestActivity.questID,
                quoteID = selectedQuote?.quoteID ?: userQuestActivity.quoteID,
                userID = userQuestActivity.userID
            )

            dbHelper.updateUserQuestActivity(updatedUQA)
            if (newStatus == "COMPLETED") {
                    UserStreakManager.updateStreakIfNeeded(this)
                }
            this.todaysSelectedUQA = updatedUQA


            calculateUserExpGainAndStatsGain(user, quest, todaysSelectedUQA)
            val updatedUser = user
            dbHelper.updateUser(updatedUser)

            Log.d("SAVING_DEBUG", "UPDATED UQA")
        }
    }

    // TODO Still need to add level and levelcap logic
    private fun calculateUserExpGainAndStatsGain(user: User, quest: Quest, uqa: UserQuestActivity) {
        val streakCount = maxOf(0, UserStreakManager.getStreakCount(this))
        val base = if (streakCount >= 3) 0.02 else 0.0
        val stepBonus = (streakCount / 5) * 0.05
        val streakMultiplier = 1.0 + base + stepBonus

        when (uqa.questStatus) {
            "COMPLETED" -> {
                user.currentExp += quest.xpReward * streakMultiplier.toFloat()

                when (quest.questType) {
                    "Strength" -> user.strengthPts += quest.statReward
                    "Endurance" -> user.endurancePts += quest.statReward
                    "Vitality" -> user.vitalityPts += quest.statReward
                }
            }

            "ABORTED" -> {
                user.currentExp -= (quest.xpReward / 2).toFloat()
            }
        }
    }

    // Optionally: if you want to double-check saving before destruction
    override fun onDestroy() {
        saveQuestStatusAndQuote(todaysSelectedUQA)
        super.onDestroy()
    }
}