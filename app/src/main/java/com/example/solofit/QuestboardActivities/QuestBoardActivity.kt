package com.example.solofit.QuestboardActivities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.R
import com.example.solofit.databinding.QuestBoardActivityBinding
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.PopupLegendBinding
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras
import java.text.SimpleDateFormat
import java.util.*

class QuestBoardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestBoardAdapter
    private lateinit var viewBinding: QuestBoardActivityBinding
    private lateinit var prefs: android.content.SharedPreferences
    private val dbHelper by lazy { MyDatabaseHelper.getInstance(this)!! }

    private var fullUQAList = listOf<UserQuestActivity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = QuestBoardActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        prefs = getSharedPreferences("QuestPrefs", MODE_PRIVATE)

        recyclerView = viewBinding.recViewQuests
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = QuestBoardAdapter(listOf(), dbHelper)
        recyclerView.adapter = adapter

        setupSpinners()

        viewBinding.imbLegend.setOnClickListener {
            showDifficultyLegendPopup()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodayCreatedQuests()
    }

    private fun setupSpinners() {
        val sortOptions = listOf("Easiest First", "Hardest First")
        val filterOptions = listOf("All", "Easy", "Normal", "Hard", "Extreme", "Strength", "Endurance", "Vitality")

        val sortAdapter = ArrayAdapter(this, R.layout.spinner_selected_blank, sortOptions)
        sortAdapter.setDropDownViewResource(R.layout.spinner_item_white)
        viewBinding.spinnerSort.adapter = sortAdapter

        val filterAdapter = ArrayAdapter(this, R.layout.spinner_selected_blank, filterOptions)
        filterAdapter.setDropDownViewResource(R.layout.spinner_item_white)
        viewBinding.spinnerFilter.adapter = filterAdapter

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applySortAndFilter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewBinding.spinnerSort.onItemSelectedListener = listener
        viewBinding.spinnerFilter.onItemSelectedListener = listener
    }

    private fun applySortAndFilter() {
        val sortOption = viewBinding.spinnerSort.selectedItem.toString()
        val filterOption = viewBinding.spinnerFilter.selectedItem.toString()

        var filtered = fullUQAList
        filtered = when (filterOption) {
            "Easy", "Normal", "Hard", "Extreme" -> filtered.filter {
                dbHelper.getQuestById(it.questID)?.difficulty == filterOption
            }
            "Strength", "Endurance", "Vitality" -> filtered.filter {
                dbHelper.getQuestById(it.questID)?.questType == filterOption
            }
            else -> filtered
        }

        if (sortOption == "Easiest First") {
            filtered = filtered.sortedBy { dbHelper.getQuestById(it.questID)?.questName ?: "" }
        } else {
            filtered = filtered.sortedByDescending { dbHelper.getQuestById(it.questID)?.questName ?: "" }
        }

        adapter.updateData(filtered)
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

        fullUQAList = dbHelper.getUserQuestsByStatusDateAndUserID(
            "CREATED", today, Extras.DEFAULT_USER_ID
        )
        applySortAndFilter()
    }



    private fun showDifficultyLegendPopup() {
        viewBinding.lloQuestboard.visibility = View.INVISIBLE
        val popupBinding = PopupLegendBinding.inflate(layoutInflater)
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupBinding.root)

        popupBinding.btnGoBack.setOnClickListener {
            viewBinding.lloQuestboard.visibility = View.VISIBLE
            rootView.removeView(popupBinding.root)
        }
    }
}