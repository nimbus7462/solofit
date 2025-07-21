package com.example.solofit.QuestboardActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.model.Quest
import com.example.solofit.databinding.QuestBoardActivityBinding
import com.example.solofit.datahelpers.QuestDataHelper
import com.example.solofit.database.MyDatabaseHelper


class QuestBoardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var questList: ArrayList<Quest>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val questBoardBinding: QuestBoardActivityBinding = QuestBoardActivityBinding.inflate(layoutInflater)
        setContentView(questBoardBinding.root)

        // Initialize the RecyclerView
        this.recyclerView = questBoardBinding.recViewQuests

        // Get quests from SQLite now instead of DataHelper
        val dbHelper = MyDatabaseHelper.getInstance(this)!!
        questList = dbHelper.getAllQuests()

        this.recyclerView.adapter = QuestBoardAdapter(this.questList)

        // Set the LayoutManager. This can be set to different kinds of LayoutManagers but we're
        // keeping things simple with a LinearLayout.
        this.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

}