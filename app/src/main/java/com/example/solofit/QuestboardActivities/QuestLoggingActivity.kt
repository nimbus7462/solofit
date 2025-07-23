package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.InitialLoggingBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuestLoggingActivity : AppCompatActivity() {

    // Instance variable not constant
    private lateinit var viewBinding: InitialLoggingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = InitialLoggingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // passed quest title from the quest board
        val questTitle = intent.getStringExtra(QuestAbortCompleteActivity.EXTRA_QUEST_NAME)
        viewBinding.txvInitLoggingQuestName.text = questTitle

        // date
        val currentDate = SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(Date())
        viewBinding.txvInitLoggingCompletedDate.text = currentDate

        viewBinding.btnFinishLog.setOnClickListener {
            finish()
        }


    }
}