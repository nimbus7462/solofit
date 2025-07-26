package com.example.solofit.QuestboardActivities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.InitialLoggingBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.solofit.model.UserQuestActivity

class QuestLoggingActivity : AppCompatActivity() {

    private lateinit var viewBinding: InitialLoggingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = InitialLoggingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Step 1: Get Quest ID from intent
        val questId = intent.getIntExtra(QuestAbortCompleteActivity.QUEST_ID_KEY, -1)
        if (questId == -1) {
            finish() // invalid quest
            return
        }

        // Step 2: Load Quest and DB Helper
        val dbHelper = MyDatabaseHelper.getInstance(this)!!
        val quest = dbHelper.getQuestById(questId) ?: run {
            finish() // quest not found
            return
        }

        // Step 3: Display quest name and today's date
        viewBinding.txvInitLoggingQuestName.text = quest.questName
        val currentDate = SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(Date())
        viewBinding.txvInitLoggingCompletedDate.text = currentDate

        // tep 4: Enable logging input and fetch UQA Data
        val logEditText = viewBinding.addLog
        val finishButton = viewBinding.btnFinishLog
        logEditText.isEnabled = true

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val matchingUQAs = dbHelper.getUserQuestsByStatusAndDate("COMPLETED", today) +
                dbHelper.getUserQuestsByStatusAndDate("ABORTED", today)
        val uqa = matchingUQAs.find { it.questID == quest.id }

        if (uqa != null) {
            logEditText.setText(uqa.userLogs ?: "")
            finishButton.isEnabled = !uqa.userLogs.isNullOrBlank()
        } else {
            finishButton.isEnabled = false
        }

        // Step 5: TextWatcher to enable/disable Finish button
        // Must type smth first to click
        logEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isBlank = s.isNullOrBlank()
                finishButton.isEnabled = !isBlank

                // Log content checker
                if (isBlank && logEditText.hasFocus()) {
                    Toast.makeText(this@QuestLoggingActivity, "Log cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Step 6: Save logs on button click
        // - if the finish logging button is clicked, save the log text to the userLogs UserQuestActivity attribute
        finishButton.setOnClickListener {
            val logText = logEditText.text.toString().trim()

            if (uqa != null) {
                val updatedUQA = UserQuestActivity(
                    userQuestActID = uqa.userQuestActID,
                    questStatus = uqa.questStatus,
                    userLogs = logText,
                    dateCreated = uqa.dateCreated,
                    questID = uqa.questID,
                    quoteID = uqa.quoteID,
                    userID = uqa.userID
                )
                dbHelper.updateUserQuestActivity(updatedUQA)
                Toast.makeText(this, "Log saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No matching quest found today.", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }
}
