package com.example.solofit.DailySummaryActivities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestSummaryBinding
import com.example.solofit.model.UserQuestActivity

class QuestSummaryActivity : AppCompatActivity() {
    private lateinit var viewBinding: QuestSummaryBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var todaysUQAList: ArrayList<UserQuestActivity>
    val dbHelper = MyDatabaseHelper.getInstance(this)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = QuestSummaryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        this.recyclerView = viewBinding.recViewQuestSummary

        todaysUQAList = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                intent.getParcelableArrayListExtra("uqaList", UserQuestActivity::class.java)
            else ->
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra("uqaList")
        } ?: arrayListOf()

        todaysUQAList.removeIf { it.questStatus.equals("CREATED", ignoreCase = true) }

        this.recyclerView.adapter = QuestSummaryAdapter(this.todaysUQAList, dbHelper)


        this.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}