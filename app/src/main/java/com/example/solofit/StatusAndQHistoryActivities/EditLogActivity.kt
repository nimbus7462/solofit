package com.example.solofit.StatusAndQHistoryActivities

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.QuestboardActivities.QuestAbortCompleteActivity
import com.example.solofit.StatusAndQHistoryActivities.EditLogActivity
import com.example.solofit.databinding.EditLogBinding
import com.example.solofit.R

class EditLogActivity : AppCompatActivity() {

    // Instance variable not constant
    private lateinit var viewBinding: EditLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = EditLogBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // passed quest title from the quest board
        val questTitle = intent.getStringExtra(QuestHistoryAdapter.EXTRA_QUEST_TITLE)
        viewBinding.txvLoggingQuestName.text = questTitle



        val userLog = intent.getStringExtra(QuestHistoryAdapter.EXTRA_USER_LOG)
        viewBinding.edtLog.setText(userLog)
        viewBinding.edtLog.isEnabled = false

        viewBinding.btnEditLog.setOnClickListener {
            viewBinding.edtLog.isEnabled = true
            viewBinding.edtLog.requestFocus()
            viewBinding.btnEditLog.visibility = View.GONE
            viewBinding.lloHideIfNotSelected.visibility = View.VISIBLE
        }

        val questStatus = intent.getStringExtra(QuestHistoryAdapter.EXTRA_QUEST_STATUS)
        val statusText = if (questStatus == "Aborted") {
            getString(R.string.aborted)
        } else {
            getString(R.string.completed)
        }

        // date
        val dateCompleted = intent.getStringExtra(QuestHistoryAdapter.EXTRA_DATE_COMPLETED)
        viewBinding.txvLoggingCompletedDate.text = dateCompleted
        viewBinding.txvLoggingQuestStatus.text = statusText

        viewBinding.btnCancelLog.setOnClickListener {
            viewBinding.edtLog.isEnabled = false
            viewBinding.btnEditLog.visibility = View.VISIBLE
            viewBinding.lloHideIfNotSelected.visibility = View.GONE
        }
        viewBinding.btnSaveLog.setOnClickListener {
            viewBinding.edtLog.isEnabled = false
            viewBinding.btnEditLog.visibility = View.VISIBLE
            viewBinding.lloHideIfNotSelected.visibility = View.GONE
            val newLog = viewBinding.edtLog.text
           //TODO: SAVING HERE
        }

    }
}