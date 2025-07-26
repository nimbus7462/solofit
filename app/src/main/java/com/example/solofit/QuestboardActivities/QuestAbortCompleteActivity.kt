package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Build
import android.os.Bundle
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

class QuestAbortCompleteActivity : AppCompatActivity() {

    private lateinit var viewBinding: AbortCompleteQuestBinding
    private var isSaved = false

    @RequiresApi(Build.VERSION_CODES.O)
    // Store the selected quote for use when saving to UQA
    private var selectedQuote: Quote? = null

    private lateinit var dbHelper: MyDatabaseHelper
    private var questId: Int = -1
    private var questStatus: String? = null
    private lateinit var quest: Quest
    private var hasSavedStatus = false // Track if save was already done
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = AbortCompleteQuestBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveQuestStatusAndQuote()
                setResult(RESULT_OK)
                finish()
            }
        })

        // Get quest info from intent
        questId = intent.getIntExtra(Extras.QUEST_ID_KEY, -1)
        questStatus = intent.getStringExtra(Extras.EXTRA_QUEST_STATUS)


        if (questId == -1 || questStatus == null) {
            finish()
            return
        }

        dbHelper = MyDatabaseHelper.getInstance(this)!!
        quest = dbHelper.getQuestById(questId) ?: run {
            finish()
            return
        }

        // Quest name shown at the top
        viewBinding.txvQStatusQuestName.text = quest.questName

        // Load quote
        loadAndDisplayQuote()

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

        // Log thoughts button
        viewBinding.btnLogThoughts.setOnClickListener {
            val intent = Intent(applicationContext, QuestLoggingActivity::class.java)
            intent.putExtra(Extras.QUEST_ID_KEY, quest.id)
            startActivity(intent)
            finish()
        }

        // Finish Quest button — Save status + quote
        viewBinding.btnFinishQuest.setOnClickListener {
            saveQuestStatusAndQuote() // Save on manual click
            setResult(RESULT_OK) // Let QuestBoardActivity know to refresh
            finish()
        }
    }

    // Load a random quote and update UI
    private fun loadAndDisplayQuote() {
        val quote = dbHelper.getRandomQuote()
        quote?.let {
            selectedQuote = it

            viewBinding.txvQuoteContent.text = "“${it.quoteText}”"
            viewBinding.txvQuoteAuthor.text = "- ${it.quoteAuthor}"

            isSaved = it.isSaved
            val icon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
            viewBinding.imbSaveQuote.setImageResource(icon)

            viewBinding.imbSaveQuote.setOnClickListener {
                isSaved = !isSaved
                val updatedIcon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
                viewBinding.imbSaveQuote.setImageResource(updatedIcon)

                selectedQuote?.let { q ->
                    dbHelper.updateQuoteSaveStatus(q.quoteID, isSaved)
                }
            }
        }
    }

    // Reusable method that updates the quest status and saves the quote ID into the UQA
    private fun saveQuestStatusAndQuote() {
        if (hasSavedStatus) return // 🔁 Don't save multiple times
        hasSavedStatus = true

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val matchingUQAs = dbHelper.getUserQuestsByStatusAndDate("CREATED", today)
        val uqa = matchingUQAs.find { it.questID == questId }

        if (uqa != null && questStatus != null) {
            val newStatus = when (questStatus) {
                 Extras.STATUS_COMPLETED -> "COMPLETED"
                 Extras.STATUS_ABORTED -> "ABORTED"
                else -> uqa.questStatus
            }

            val updatedUQA = UserQuestActivity(
                userQuestActID = uqa.userQuestActID,
                questStatus = newStatus,
                userLogs = uqa.userLogs,
                dateCreated = uqa.dateCreated,
                questID = uqa.questID,
                quoteID = selectedQuote?.quoteID ?: uqa.quoteID,
                userID = uqa.userID
            )

            dbHelper.updateUserQuestActivity(updatedUQA)
            if (newStatus == "COMPLETED") {
                    UserStreakManager.updateStreakIfNeeded(this)
                }
        }
    }

    // Optionally: if you want to double-check saving before destruction
    override fun onDestroy() {
        saveQuestStatusAndQuote()
        super.onDestroy()
    }
}