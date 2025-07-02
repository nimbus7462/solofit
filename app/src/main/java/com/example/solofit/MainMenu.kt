package com.example.solofit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.solofit.databinding.MainMenuBinding

class MainMenu : AppCompatActivity() {

    private lateinit var binding: MainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the ViewBinding
        binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button click now starts a new Activity instead of navigating fragments
        binding.manageQuestBtnImg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        binding.questBoard.setOnClickListener{
            val intent = Intent(this, QuestBoardActivity::class.java)

            startActivity(intent)
        }


    }
}
