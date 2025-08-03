package com.example.solofit.DailySummaryActivities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.PopupLegendBinding
import com.example.solofit.databinding.QuestSummaryBinding
import com.example.solofit.model.UserQuestActivity

class QuestSummaryActivity : AppCompatActivity() {

    private lateinit var viewBinding: QuestSummaryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestSummaryAdapter
    private lateinit var fullUQAList: List<UserQuestActivity>
    private val dbHelper by lazy { MyDatabaseHelper.getInstance(this)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = QuestSummaryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        recyclerView = viewBinding.recViewQuestSummary
        recyclerView.layoutManager = LinearLayoutManager(this)

        val rawList: ArrayList<UserQuestActivity> = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                intent.getParcelableArrayListExtra("uqaList", UserQuestActivity::class.java)
            else -> @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("uqaList")
        } ?: arrayListOf()

        // Only show completed/cancelled quests, not fresh CREATED
        fullUQAList = rawList.filterNot {
            it.questStatus.equals("CREATED", ignoreCase = true) ||
                    it.questStatus.equals("DELETED", ignoreCase = true)
        }

        adapter = QuestSummaryAdapter(fullUQAList, dbHelper)
        recyclerView.adapter = adapter

        setupSpinners()

        viewBinding.imbLegend.setOnClickListener {
            showDifficultyLegendPopup()
        }
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

        filtered = when (sortOption) {
            "Easiest First" -> filtered.sortedBy { dbHelper.getQuestById(it.questID)?.questName ?: "" }
            "Hardest First" -> filtered.sortedByDescending { dbHelper.getQuestById(it.questID)?.questName ?: "" }
            else -> filtered
        }

        adapter.updateData(filtered)
    }


    private fun showDifficultyLegendPopup() {
        val popupBinding = PopupLegendBinding.inflate(layoutInflater)
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupBinding.root)

        popupBinding.btnGoBack.setOnClickListener {
            rootView.removeView(popupBinding.root)
        }
    }
}
