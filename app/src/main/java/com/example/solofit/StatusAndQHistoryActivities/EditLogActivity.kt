package com.example.solofit.StatusAndQHistoryActivities

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.QuestboardActivities.QuestAbortCompleteActivity
import com.example.solofit.StatusAndQHistoryActivities.EditLogActivity
import com.example.solofit.databinding.EditLogBinding
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras

class EditLogActivity : AppCompatActivity() {

    // Instance variable not constant
    private lateinit var viewBinding: EditLogBinding
    private lateinit var originalLog: String
    private lateinit var uqa: UserQuestActivity
    val dbHelper = MyDatabaseHelper.getInstance(this)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = EditLogBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // passed quest title from the quest board
        val questTitle = intent.getStringExtra(Extras.EXTRA_QUEST_TITLE)
        viewBinding.txvLoggingQuestName.text = questTitle


        val uqaId = intent.getIntExtra(Extras.EXTRA_UQA_ID, -1)
        if (uqaId == -1) {
            finish()
            return
        }

        uqa = dbHelper.getUserQuestActivityById(uqaId) ?: run {
            finish()
            return
        }

        originalLog = uqa.userLogs
        viewBinding.edtLog.setText(originalLog)
        viewBinding.edtLog.isEnabled = false


        viewBinding.btnEditLog.setOnClickListener {
            viewBinding.edtLog.isEnabled = true
            viewBinding.edtLog.requestFocus()
            viewBinding.btnEditLog.visibility = View.GONE
            viewBinding.lloHideIfNotSelected.visibility = View.VISIBLE
        }

        val questStatus = uqa.questStatus
        val statusText = if (questStatus == "ABORTED") {
            getString(R.string.aborted)
        } else {
            getString(R.string.completed)
        }

        // date
        val dateCompleted = intent.getStringExtra(Extras.EXTRA_DATE_COMPLETED)
        viewBinding.txvLoggingCompletedDate.text = dateCompleted
        viewBinding.txvLoggingQuestStatus.text = statusText

        viewBinding.btnCancelLog.setOnClickListener {
            viewBinding.edtLog.isEnabled = false
            viewBinding.btnEditLog.visibility = View.VISIBLE
            viewBinding.lloHideIfNotSelected.visibility = View.GONE
        }

        viewBinding.btnSaveLog.setOnClickListener {
            val newLog = viewBinding.edtLog.text.toString().trim()
            if (newLog == originalLog) {
                Toast.makeText(this, "No changes made.", Toast.LENGTH_SHORT).show()
            } else {
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
                Toast.makeText(this, "Log updated successfully!", Toast.LENGTH_SHORT).show()
                originalLog = newLog // update reference
            }

            // Disable edit mode
            viewBinding.edtLog.isEnabled = false
            viewBinding.btnEditLog.visibility = View.VISIBLE
            viewBinding.lloHideIfNotSelected.visibility = View.GONE
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentLog = viewBinding.edtLog.text.toString().trim()
                if (currentLog != originalLog && viewBinding.edtLog.isEnabled) {
                    Toast.makeText(this@EditLogActivity, "Changes discarded.", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        })
    }

}