package com.example.solofit.StatusAndQHistoryActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.StatusPageBinding

class StatusActivity: AppCompatActivity() {
    private lateinit var viewBinding: StatusPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = StatusPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnQuestHistory.setOnClickListener {
            val intent = Intent(this, QuestHistoryActivity::class.java)
            startActivity(intent)
        }

    }
}