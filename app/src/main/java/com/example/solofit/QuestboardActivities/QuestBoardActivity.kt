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
    private lateinit var prefs: android.content.SharedPreferences
    private val dbHelper by lazy { MyDatabaseHelper.getInstance(this)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = QuestBoardActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        prefs = getSharedPreferences("QuestPrefs", MODE_PRIVATE)

        recyclerView = viewBinding.recViewQuests
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        loadTodayCreatedQuests()
    }

    private fun loadTodayCreatedQuests() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastQuestDate = prefs.getString("lastQuestDate", null)

        dbHelper.autoCancelOldUnfinishedQuests(today)

        val existingCreatedQuests = dbHelper.getUserQuestsByStatusDateAndUserID(
            "CREATED", today, Extras.DEFAULT_USER_ID
        )

        if (lastQuestDate != today && existingCreatedQuests.isEmpty()) {
            val randomQuests = dbHelper.getRandomQuests(5)
            for (quest in randomQuests) {
                val uqa = UserQuestActivity(
                    questStatus = "CREATED",
                    userLogs = "",
                    dateCreated = today,
                    questID = quest.id,
                    quoteID = 0,
                    userID = Extras.DEFAULT_USER_ID
                )
                dbHelper.insertUserQuestActivity(uqa)
            }

            prefs.edit().putString("lastQuestDate", today).apply()
        }

        val updatedCreatedQuests = dbHelper.getUserQuestsByStatusDateAndUserID(
            "CREATED", today, Extras.DEFAULT_USER_ID
        )
        val fullQuestDetails = updatedCreatedQuests.mapNotNull { dbHelper.getQuestById(it.questID) }

        questList = ArrayList(fullQuestDetails)
        recyclerView.adapter = QuestBoardAdapter(updatedCreatedQuests, dbHelper)
    }
}
