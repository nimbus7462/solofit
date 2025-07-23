package com.example.solofit.StatusAndQHistoryActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.QuestboardActivities.QuestBoardAdapter
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestHistoryBinding
import com.example.solofit.datahelpers.QuestDataHelper
import com.example.solofit.datahelpers.UserQuestActivityDataHelper
import com.example.solofit.model.Quest
import com.example.solofit.model.UserQuestActivity

class QuestHistoryActivity: AppCompatActivity()  {
    private lateinit var viewBinding: QuestHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private val questList: ArrayList<Quest> = QuestDataHelper.getQuestsFromUserQuestActivities()
    private val userQuestActList: ArrayList<UserQuestActivity> = UserQuestActivityDataHelper.initUQA()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = QuestHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Initialize the RecyclerView
        this.recyclerView = viewBinding.recViewQuestHistory

        this.recyclerView.adapter = QuestHistoryAdapter(this.questList, this.userQuestActList)

        // Set the LayoutManager. This can be set to different kinds of LayoutManagers but we're
        // keeping things simple with a LinearLayout.
        this.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }

}