package com.example.solofit.QuestboardActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.InitialLoggingBinding
import com.example.solofit.utilities.Extras
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuestLoggingActivity : AppCompatActivity() {

    private lateinit var viewBinding: InitialLoggingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = InitialLoggingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val questId = intent.getIntExtra(Extras.QUEST_ID_KEY, -1)
        if (questId == -1) {
            finish() // invalid id
            return
        }

        // Retrieve Quest data using ID
        val dbHelper = MyDatabaseHelper.getInstance(this)!!
        val quest = dbHelper.getQuestById(questId) ?: run {
            finish() // quest not found
            return
        }

        // Show quest name
        viewBinding.txvInitLoggingQuestName.text = quest.questName

        // Show current date
        val currentDate = SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(Date())
        viewBinding.txvInitLoggingCompletedDate.text = currentDate

        viewBinding.btnFinishLog.setOnClickListener {
            finish()
        }
    }
}
