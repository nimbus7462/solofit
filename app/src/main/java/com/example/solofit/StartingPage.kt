package com.example.solofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.StartingPageBinding
import java.util.concurrent.Executors

class StartingPage : AppCompatActivity() {

    private lateinit var binding: StartingPageBinding
    private lateinit var myDbHelper: MyDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the binding and set the root view
        binding = StartingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Log.d("StartingPage", "StartingPage launched")

        // creates the database as soon as the app is launched
        myDbHelper = MyDatabaseHelper.getInstance(this@StartingPage)!!

        // Button click listener
        binding.btnContinue.setOnClickListener {
            // If you're navigating to another activity, use Intent
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
    }
}
