package com.example.solofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.DailySummaryPageBinding


class DailySummaryActivity: AppCompatActivity() {
    private lateinit var binding: DailySummaryPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DailySummaryPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}