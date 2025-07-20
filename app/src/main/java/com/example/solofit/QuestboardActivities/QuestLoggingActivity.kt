package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.InitialLoggingBinding

class QuestLoggingActivity : AppCompatActivity() {

    // Instance variable not constant
    private lateinit var viewBinding: InitialLoggingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = InitialLoggingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnFinishLog.setOnClickListener {
            finish()
        }


    }
}