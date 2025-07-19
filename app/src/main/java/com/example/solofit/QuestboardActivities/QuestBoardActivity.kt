package com.example.solofit.QuestboardActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.solofit.model.Quest
import com.example.solofit.databinding.QuestBoardActivityBinding


class QuestBoardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val questList: ArrayList<Quest> = QuestDataHelper.initializeQuests()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()

        val questBoardBinding: QuestBoardActivityBinding = QuestBoardActivityBinding.inflate(layoutInflater)
        setContentView(questBoardBinding.root)

        // Initialize the RecyclerView
        this.recyclerView = questBoardBinding.recViewQuests


        // Set the Adapter. We have to define our own Adapter so that we can properly set the
        // information into the item layout we created. It is typical to pass the data we want
        // displayed into the adapter. There are other variants of RecyclerViews that query data
        // from online sources in batches (instead of passing everything), but we'll get to that
        // when we reach accessing remote DBs.
        this.recyclerView.adapter = QuestBoardAdapter(this.questList)

        // Set the LayoutManager. This can be set to different kinds of LayoutManagers but we're
        // keeping things simple with a LinearLayout.
        this.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val helper: SnapHelper = LinearSnapHelper()
        helper.attachToRecyclerView(recyclerView)
    }

}