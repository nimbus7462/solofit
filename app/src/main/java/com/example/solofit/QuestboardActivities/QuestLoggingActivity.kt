package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.QuestLoggingBinding

class QuestLoggingActivity : AppCompatActivity() {

    // Instance variable not constant
    private lateinit var viewBinding: QuestLoggingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = QuestLoggingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnLogLogging.setOnClickListener {
            val intentLoggingActivity = Intent(applicationContext, QuestBoardActivity::class.java)


            this.startActivity(intentLoggingActivity)

        }


    }
}