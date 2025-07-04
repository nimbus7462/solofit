package com.example.solofit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.QuestCompletedBinding

class QuestCompletedActivity : AppCompatActivity(){

    // Instance variable not constant
    private lateinit var viewBinding: QuestCompletedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = QuestCompletedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnLogCompleted.setOnClickListener {
            val intentLoggingActivity = Intent(applicationContext, QuestLoggingActivity::class.java)


            this.startActivity(intentLoggingActivity)

        }

        viewBinding.btnReturnCompleted.setOnClickListener {
            val intentQuestBoardActivity = Intent(applicationContext, QuestBoardActivity::class.java)


            this.startActivity(intentQuestBoardActivity)

        }

    }

}