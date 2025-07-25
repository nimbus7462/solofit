package com.example.solofit.QuestboardActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestBoardActivityBinding
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.model.Quest
import com.example.solofit.model.UserQuestActivity
import java.text.SimpleDateFormat
import java.util.*

class QuestBoardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var questList: ArrayList<Quest>
    private lateinit var viewBinding: QuestBoardActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = QuestBoardActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize the RecyclerView
        recyclerView = viewBinding.recViewQuests

        // Set the LayoutManager. This can be set to different kinds of LayoutManagers but we're
        // keeping things simple with a LinearLayout.
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // TODO:
        /*
        - Gets 5 quest from the db randomly
        - save it to the UserQuestActivity table DB
        - then, it sets the questStatus UserQuestActivity attribute of each quest to "CREATED"
        - Note: only quest with "CREATED" will be shown here, not "CANCELLED" or "COMPLETED"

        - Each new day, 5 random quest would be given.
        - If the end of the day and the quest is not cancelled or completed its automatically "CANCELLED"
         */

        // Step 1: Initialize DB
        val dbHelper = MyDatabaseHelper.getInstance(this)!!

        // Step 2: Check today's date
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Step 3: Cancel uncompleted previous quests
        dbHelper.autoCancelOldUnfinishedQuests(today)

        // Step 4: Check if today’s quests were already generated
        val existingCreatedQuests = dbHelper.getUserQuestsByStatusAndDate("CREATED", today)

        if (existingCreatedQuests.isEmpty()) {
            // Step 5: Fetch 5 random quests
            val randomQuests = dbHelper.getRandomQuests(5)

            // Step 6: Insert each into UserQuestActivity with "CREATED" status
            for (quest in randomQuests) {
                val uqa = UserQuestActivity(
                    questStatus = "CREATED",
                    userLogs = "",
                    dateCreated = today,
                    questID = quest.id,
                    quoteID = 0, // default, will be updated later
                    userID = 1 // or dynamically get userId
                )
                dbHelper.insertUserQuestActivity(uqa)
            }
        }

        // Step 7: Load CREATED quests for today and get full Quest data
        val todaysCreatedUQAs = dbHelper.getUserQuestsByStatusAndDate("CREATED", today)
        val createdQuestDetails = todaysCreatedUQAs.mapNotNull { dbHelper.getQuestById(it.questID) }

        // Step 8: Populate RecyclerView
        questList = ArrayList(createdQuestDetails)
        recyclerView.adapter = QuestBoardAdapter(questList)
    }
}
