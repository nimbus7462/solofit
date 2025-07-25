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
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.UserStreakManager
import java.text.SimpleDateFormat
import java.util.*

class QuestAbortCompleteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_QUEST_STATUS = "quest_status"
        const val STATUS_COMPLETED = "completed"
        const val STATUS_ABORTED = "aborted"
        const val QUEST_ID_KEY = "quest_id"
    }

    private lateinit var viewBinding: AbortCompleteQuestBinding
    private var isSaved = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = AbortCompleteQuestBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val questId = intent.getIntExtra(QUEST_ID_KEY, -1)
        val questStatus = intent.getStringExtra(EXTRA_QUEST_STATUS)

        if (questId == -1 || questStatus == null) {
            finish()
            return
        }

        val dbHelper = MyDatabaseHelper.getInstance(this)!!
        val quest: Quest = dbHelper.getQuestById(questId) ?: run {
            finish()
            return
        }

        // Quest name shown at the top
        viewBinding.txvQStatusQuestName.text = quest.questName

        // TODO: Load and display a random quote
        val quote = dbHelper.getRandomQuote()
        quote?.let { selectedQuote ->
            viewBinding.txvQuoteContent.text = "“${selectedQuote.quoteText}”"
            viewBinding.txvQuoteAuthor.text = "- ${selectedQuote.quoteAuthor}"

            isSaved = selectedQuote.isSaved
            val icon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
            viewBinding.imbSaveQuote.setImageResource(icon)

            viewBinding.imbSaveQuote.setOnClickListener {
                isSaved = !isSaved
                val updatedIcon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
                viewBinding.imbSaveQuote.setImageResource(updatedIcon)

                // Now this works:
                dbHelper.updateQuoteSaveStatus(selectedQuote.quoteID, isSaved)
            }
        }

        when (questStatus) {
            STATUS_COMPLETED -> {
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

            STATUS_ABORTED -> {
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

        viewBinding.btnLogThoughts.setOnClickListener {
            val intent = Intent(applicationContext, QuestLoggingActivity::class.java)
            intent.putExtra(QUEST_ID_KEY, quest.id)
            startActivity(intent)
            finish()
        }

        // TODO: once the Cancel or Complete btn is clicked, set the questStatus UserQuestActivity attribute to "ABORTED" or "COMPLETED" depending on the action
        viewBinding.btnFinishQuest.setOnClickListener {
            // STEP 1: Get today’s date
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            // STEP 2: Get UQA for this quest and today
            val matchingUQAs = dbHelper.getUserQuestsByStatusAndDate("CREATED", today)
            val uqa = matchingUQAs.find { it.questID == quest.id }

            // STEP 3: Update status if match found
            if (uqa != null) {
                val newStatus = when (questStatus) {
                    STATUS_COMPLETED -> "COMPLETED"
                    STATUS_ABORTED -> "ABORTED"
                    else -> uqa.questStatus // fallback, shouldn't happen
                }

                // STEP 3.1 Manually create a new UQA object with updated status
                val updatedUQA = UserQuestActivity(
                    userQuestActID = uqa.userQuestActID,
                    questStatus = newStatus,
                    userLogs = uqa.userLogs,
                    dateCreated = uqa.dateCreated,
                    questID = uqa.questID,
                    quoteID = uqa.quoteID,
                    userID = uqa.userID
                )

                // STEP 3.2 Update the DB
                dbHelper.updateUserQuestActivity(updatedUQA)

                // STEP 3.3 Update streak if completed
                if (newStatus == "COMPLETED") {
                    UserStreakManager.updateStreakIfNeeded(this)
                }
            }

            // STEP 4: Finish activity
            finish()
        }

        var isSaved = false
        viewBinding.imbSaveQuote.setOnClickListener {
            isSaved = !isSaved
            val icon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
            viewBinding.imbSaveQuote.setImageResource(icon)
        }
    }
}
