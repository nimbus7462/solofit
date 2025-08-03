package com.example.solofit.StatusAndQHistoryActivities

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
import com.example.solofit.databinding.QuestHistoryBinding
import com.example.solofit.model.UserQuestActivity

class QuestHistoryActivity : AppCompatActivity() {
    private lateinit var viewBinding: QuestHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestHistoryAdapter
    private val dbHelper by lazy { MyDatabaseHelper.getInstance(this)!! }
    private var fullUQAList = listOf<UserQuestActivity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = QuestHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        recyclerView = viewBinding.recViewQuestHistory
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = QuestHistoryAdapter(listOf(), dbHelper)
        recyclerView.adapter = adapter

        setupSpinners()
        viewBinding.imbLegend.setOnClickListener {
            showQHistLegendPopup()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCompletedAndCancelledQuests()
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

        filtered = if (sortOption == "Easiest First") {
            filtered.sortedBy { dbHelper.getQuestById(it.questID)?.questName ?: "" }
        } else {
            filtered.sortedByDescending { dbHelper.getQuestById(it.questID)?.questName ?: "" }
        }

        adapter.updateData(filtered)
    }

    private fun loadCompletedAndCancelledQuests() {
        fullUQAList = dbHelper.getAllUserQuestActivities()
            .filter { it.questStatus.equals("COMPLETED", true) || it.questStatus.equals("ABORTED", true) }
        applySortAndFilter()
    }

    private fun showQHistLegendPopup() {
        val popupBinding = PopupLegendBinding.inflate(layoutInflater)
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupBinding.root)

        popupBinding.txvDiffLegendHeader.text = getString(R.string.qhist_legend)
        popupBinding.lloShowIfQuestHist.visibility = View.VISIBLE
        popupBinding.lloDiffRow1.visibility = View.GONE
        popupBinding.lloDiffRow2.visibility = View.GONE
        popupBinding.btnGoBack.setOnClickListener {
            rootView.removeView(popupBinding.root)
        }
    }
}
