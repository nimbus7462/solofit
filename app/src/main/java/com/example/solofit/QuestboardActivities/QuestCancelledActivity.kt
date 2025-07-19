package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.QuestCancelledBinding


class QuestCancelledActivity : AppCompatActivity(){

    // Instance variable not constant
    private lateinit var viewBinding: QuestCancelledBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = QuestCancelledBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnLogCancelled.setOnClickListener {
            val intentLoggingActivity = Intent(applicationContext, QuestLoggingActivity::class.java)


            this.startActivity(intentLoggingActivity)

        }

        viewBinding.btnReturnCancelled.setOnClickListener {
            val intentQuestBoardActivity = Intent(applicationContext, QuestBoardActivity::class.java)


            this.startActivity(intentQuestBoardActivity)

        }


    }



}