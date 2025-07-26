package com.example.solofit.QuestboardActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestBoardActivityBinding
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.model.Quest
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras
import java.text.SimpleDateFormat
import java.util.*

class QuestBoardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var questList: ArrayList<Quest>
    private lateinit var viewBinding: QuestBoardActivityBinding
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = QuestBoardActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize RecyclerView
        recyclerView = viewBinding.recViewQuests
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        loadTodayCreatedQuests() // Refresh list when returning to activity
    }

    private fun loadTodayCreatedQuests() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Cancel previous day's uncompleted quests
        dbHelper.autoCancelOldUnfinishedQuests(today)

        // Check if today's CREATED quests already exist
        val existingCreatedQuests = dbHelper.getUserQuestsByStatusDateAndUserID("CREATED", today, Extras.DEFAULT_USER_ID)

        if (existingCreatedQuests.isEmpty()) {
            val randomQuests = dbHelper.getRandomQuests(5)
            for (quest in randomQuests) {
                val uqa = UserQuestActivity(
                    questStatus = "CREATED",
                    userLogs = "",
                    dateCreated = today,
                    questID = quest.id,
                    quoteID = 0, // placeholder, can be updated after completion
                    userID = Extras.DEFAULT_USER_ID // replace if dynamic user handling is added
                )
                dbHelper.insertUserQuestActivity(uqa)
            }
        }

        // Fetch the newly created (or already existing) quests for today
        val todaysUQAs = dbHelper.getUserQuestsByStatusDateAndUserID("CREATED", today, Extras.DEFAULT_USER_ID)
        val fullQuestDetails = todaysUQAs.mapNotNull { dbHelper.getQuestById(it.questID) }

        questList = ArrayList(fullQuestDetails)
        recyclerView.adapter = QuestBoardAdapter(todaysUQAs, dbHelper)
    }
}
