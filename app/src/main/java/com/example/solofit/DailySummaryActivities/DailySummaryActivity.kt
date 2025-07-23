package com.example.solofit.DailySummaryActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.DailySummaryPageBinding

class DailySummaryActivity: AppCompatActivity() {
    private lateinit var viewBinding: DailySummaryPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DailySummaryPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        viewBinding.btnQuestSummary.setOnClickListener {
            val intent = Intent(this, QuestSummaryActivity::class.java)
            startActivity(intent)
        }

    }
}