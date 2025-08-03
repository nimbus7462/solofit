package com.example.solofit.StatusAndQHistoryActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestHistoryBinding
import com.example.solofit.model.UserQuestActivity

class QuestHistoryActivity: AppCompatActivity()  {
    private lateinit var viewBinding: QuestHistoryBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var allUQAList: ArrayList<UserQuestActivity>
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = QuestHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        this.recyclerView = viewBinding.recViewQuestHistory

        allUQAList = dbHelper.getAllUserQuestActivities()
        allUQAList.removeIf { it.questStatus.equals("CREATED", ignoreCase = true) }
        this.recyclerView.adapter = QuestHistoryAdapter(this.allUQAList, this.dbHelper)

        this.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }

}