package com.example.solofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.SavedQuotesPageBinding
class QuotesActivity: AppCompatActivity() {
    private lateinit var binding: SavedQuotesPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SavedQuotesPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}