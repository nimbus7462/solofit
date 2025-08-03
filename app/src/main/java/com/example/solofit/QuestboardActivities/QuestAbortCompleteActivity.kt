package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.AbortCompleteQuestBinding
import com.example.solofit.model.Quest
import com.example.solofit.model.Quote
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.solofit.SettingsActivities.SettingsActivity
import com.example.solofit.databinding.PopupCongratsBinding
import com.example.solofit.model.User
import com.example.solofit.utilities.TitleManager

class QuestAbortCompleteActivity : AppCompatActivity() {

    private lateinit var viewBinding: AbortCompleteQuestBinding
    private var isSaved = false

    // commit test
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
                updateUserStatsAndLevel {
                    setResult(RESULT_OK)
                    finish()
                }
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
            saveQuestStatusAndQuote(todaysSelectedUQA)
            updateUserStatsAndLevel {
                setResult(RESULT_OK)
                val intent = Intent(applicationContext, QuestLoggingActivity::class.java)
                intent.putExtra(Extras.EXTRA_UQA, todaysSelectedUQA)
                startActivity(intent)
                finish()
            }
        }

        // Finish Quest button — Save status + quote
        viewBinding.btnFinishQuest.setOnClickListener {
            saveQuestStatusAndQuote(todaysSelectedUQA)
            updateUserStatsAndLevel {
                setResult(RESULT_OK)
                finish()
            }
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
                user = dbHelper.updateUserStreakIfNeeded(user)
            }
            dbHelper.updateUser(user)
            this.todaysSelectedUQA = updatedUQA

            Log.d("SAVING_DEBUG", "UPDATED UQA")
        }
    }

    private fun updateUserStatsAndLevel(onDone: () -> Unit) {
        val oldLevel = user.level
        calculateUserExpGainAndStatsGain(user, quest, todaysSelectedUQA)

        val newLevel = user.level
        val leveledUp = newLevel > oldLevel

        val unlockedTitles = TitleManager.getNewlyUnlockedTitles(user)
        if (unlockedTitles.isNotEmpty()) {
            TitleManager.checkAndUnlockTitles(user)
        }

        dbHelper.updateUser(user)

        if (leveledUp || unlockedTitles.isNotEmpty()) {
            showLevelAndTitlePopups(
                leveledUp = leveledUp,
                oldLevel = oldLevel,
                newLevel = newLevel,
                unlockedTitles = unlockedTitles,
                onDone = onDone
            )
        } else {
            onDone()
        }
    }


    private fun calculateUserExpGainAndStatsGain(user: User, quest: Quest, uqa: UserQuestActivity) {
        val streakCount = maxOf(0, user.streakCount)
        val base = if (streakCount >= 3) 0.02 else 0.0
        val stepBonus = (streakCount / 5) * 0.05
        val streakMultiplier = 1.0 + base + stepBonus

        val expGained: Float
        val statGained: Int

        when (uqa.questStatus) {
            "COMPLETED" -> {
                expGained = quest.xpReward * streakMultiplier.toFloat()
                statGained = quest.statReward

                user.currentExp += expGained

                //For stat gains
                when (quest.questType) {
                    "Strength" -> user.strengthPts += statGained
                    "Endurance" -> user.endurancePts += statGained
                    "Vitality" -> user.vitalityPts += statGained
                }

                //For Level-ups
                while (user.currentExp >= user.expCap) {
                    user.currentExp -= user.expCap
                    user.level += 1
                    user.expCap = (user.expCap * 1.2f).toInt()
                }
            }

            "ABORTED" -> {
                user.currentExp = maxOf(0f, user.currentExp - (quest.xpReward / 2f))
            }
        }
    }

    private fun showLevelAndTitlePopups(
        leveledUp: Boolean,
        oldLevel: Int,
        newLevel: Int,
        unlockedTitles: List<String>,
        onDone: () -> Unit,
    ) {
        fun showCongratsPopup(
            upperMsg: String,
            lowerMsg: String,
            isAboutTitles: Boolean,
            onPopupDone: () -> Unit
        ) {
            val popupBinding = PopupCongratsBinding.inflate(LayoutInflater.from(this))

            popupBinding.txvUpperMsg.text = upperMsg
            popupBinding.txvLowerMsg.text = lowerMsg

            val rootView = findViewById<ViewGroup>(android.R.id.content)
            rootView.addView(popupBinding.root)

            if (isAboutTitles) {
                popupBinding.btnViewTitles.visibility = View.VISIBLE
                popupBinding.btnViewTitles.setOnClickListener {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra(Extras.EXTRA_SRC, "Quest_Abort_Complete") // optional
                    startActivity(intent)
                    finish()
                }
            }

            popupBinding.btnDone.setOnClickListener {
                rootView.removeView(popupBinding.root)
                onPopupDone()
            }
        }

        if (leveledUp && unlockedTitles.isNotEmpty()) {
            showCongratsPopup(
                upperMsg = "You leveled up!",
                lowerMsg = "LEVEL $oldLevel -> $newLevel",
                isAboutTitles = false,
                onPopupDone = {
                    showCongratsPopup(
                        upperMsg = "You unlocked new title(s)!",
                        lowerMsg = unlockedTitles.joinToString("\n"),
                        isAboutTitles = true,
                        onPopupDone = onDone
                    )
                },

            )
        } else if (leveledUp) {
            showCongratsPopup(
                upperMsg = "You leveled up!",
                lowerMsg = "LEVEL $oldLevel -> $newLevel",
                onPopupDone = onDone,
                isAboutTitles = false
            )
        } else {
            showCongratsPopup(
                upperMsg = "You unlocked new title(s)!",
                lowerMsg = unlockedTitles.joinToString("\n"),
                onPopupDone = onDone,
                isAboutTitles = true
            )
        }
    }


    // Optionally: if you want to double-check saving before destruction
    override fun onDestroy() {
        saveQuestStatusAndQuote(todaysSelectedUQA)
        super.onDestroy()
    }
}