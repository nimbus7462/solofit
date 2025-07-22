package com.example.solofit.DailySummaryActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestSummaryBinding
import com.example.solofit.datahelpers.QuestDataHelper
import com.example.solofit.datahelpers.UserQuestActivityDataHelper
import com.example.solofit.model.Quest
import com.example.solofit.model.UserQuestActivity

class QuestSummaryActivity : AppCompatActivity() {
    private lateinit var viewBinding: QuestSummaryBinding
    private lateinit var recyclerView: RecyclerView
    private val questList: ArrayList<Quest> = QuestDataHelper.getQuestsFromUserQuestActivities()
    private val userQuestActList: ArrayList<UserQuestActivity> = UserQuestActivityDataHelper.initUQA()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = QuestSummaryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        this.recyclerView = viewBinding.recViewQuestSummary

        // Not Using DB at the moment
       // val dbHelper = MyDatabaseHelper.getInstance(this)!!

        this.recyclerView.adapter = QuestSummaryAdapter(this.questList, this.userQuestActList)


        this.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}