package com.example.solofit.QuoteActivities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestBoardActivityBinding
import com.example.solofit.databinding.SavedQuotesPageBinding
import com.example.solofit.model.Quest
import com.example.solofit.model.Quote
import java.util.ArrayList

class QuoteActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var quoteList: ArrayList<Quote>
    private lateinit var viewBinding: SavedQuotesPageBinding
    private lateinit var quoteAdapter: QuoteAdapter
    private lateinit var dbHelper: MyDatabaseHelper


    private lateinit var binding: SavedQuotesPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = SavedQuotesPageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        dbHelper = MyDatabaseHelper.getInstance(this)!!
        recyclerView = viewBinding.recViewSavedQuotes
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        loadQuotes()
    }
    private fun loadQuotes(){
        quoteList = dbHelper.getAllSavedQuotes()
        if (quoteList.isNotEmpty()) {
            quoteAdapter = QuoteAdapter(quoteList, dbHelper)
            recyclerView.adapter = quoteAdapter
        } else {
            Toast.makeText(this, "No saved quotes found.", Toast.LENGTH_SHORT).show()
            recyclerView.adapter = null
        }
    }


}

