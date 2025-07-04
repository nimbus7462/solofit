package com.example.solofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.StatusPageBinding

class StatusActivity: AppCompatActivity() {
    private lateinit var binding: StatusPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StatusPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}