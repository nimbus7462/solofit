package com.example.solofit.QuestboardActivities

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.InitialLoggingBinding
import com.example.solofit.utilities.Extras
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.solofit.model.UserQuestActivity

class QuestLoggingActivity : AppCompatActivity() {

    private lateinit var viewBinding: InitialLoggingBinding
    private lateinit var uqa: UserQuestActivity
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = InitialLoggingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveLogIfNeeded()
                finish()
            }
        })

        // Step 1: Get Quest ID from intent
        val todaysSelectedUQA = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                intent.getParcelableExtra(Extras.EXTRA_UQA, UserQuestActivity::class.java)
            else ->
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Extras.EXTRA_UQA)
        } ?: UserQuestActivity()

        if (todaysSelectedUQA.questID == -1) {
            finish() // invalid quest
            return
        }

        // Step 2: Load Quest and DB Helper
        val quest = dbHelper.getQuestById(todaysSelectedUQA.questID) ?: run {
            finish() // quest not found
            return
        }

        this.uqa = todaysSelectedUQA

        // Step 3: Display quest name and today's date
        viewBinding.txvInitLoggingQuestName.text = quest.questName
        val currentDate = SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(Date())
        viewBinding.txvInitLoggingCompletedDate.text = currentDate

        val statusText = when (uqa.questStatus) {
            "ABORTED" -> getString(R.string.aborted)
            "COMPLETED" -> getString(R.string.completed)
            else -> {"INVALID"}
        }
        viewBinding.txvInitLoggingQuestStatus.text = statusText

        // Step 4: Enable logging input and fetch UQA Data
        val logEditText = viewBinding.addLog
        val finishButton = viewBinding.btnFinishLog
        logEditText.isEnabled = true

        logEditText.setText(todaysSelectedUQA.userLogs ?: "")
        finishButton.isEnabled = todaysSelectedUQA.userLogs.isNotBlank()

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

            if (todaysSelectedUQA != null) {
                val updatedUQA = UserQuestActivity(
                    userQuestActID = todaysSelectedUQA.userQuestActID,
                    questStatus = todaysSelectedUQA.questStatus,
                    userLogs = logText,
                    dateCreated = todaysSelectedUQA.dateCreated,
                    questID = todaysSelectedUQA.questID,
                    quoteID = todaysSelectedUQA.quoteID,
                    userID = todaysSelectedUQA.userID
                )
                dbHelper.updateUserQuestActivity(updatedUQA)
                Toast.makeText(this, "Log saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No matching quest found today.", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    private fun saveLogIfNeeded() {
        val newLog = viewBinding.addLog.text.toString().trim()
        if (newLog.isNotBlank()) {
            val updatedUQA = UserQuestActivity(
                userQuestActID = uqa.userQuestActID,
                questStatus = uqa.questStatus,
                userLogs = newLog,
                dateCreated = uqa.dateCreated,
                questID = uqa.questID,
                quoteID = uqa.quoteID,
                userID = uqa.userID
            )
            dbHelper.updateUserQuestActivity(updatedUQA)
            Toast.makeText(this, "Log saved!", Toast.LENGTH_SHORT).show()
        }
    }

}
