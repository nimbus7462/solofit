package com.example.solofit.QuoteActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestInfoBinding
import com.example.solofit.databinding.ViewSavedQuoteBinding
import com.example.solofit.model.Quote
import com.example.solofit.utilities.Extras

class ViewSavedQuote : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ViewSavedQuoteBinding = ViewSavedQuoteBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val quoteId = intent.getIntExtra(Extras.QUOTE_ID, -1)
        if (quoteId == -1) {
            finish() // Invalid ID, exit early
            return
        }
        val dbHelper = MyDatabaseHelper.getInstance(this)!!
        val quote: Quote = dbHelper?.getQuoteById(quoteId) ?: run {
            finish() // Quote not found
            return
        }
        viewBinding.tvQuoteContent.text = quote.quoteText
        viewBinding.tvQuoteAuthor.text = quote.quoteAuthor
//        viewBinding.ivSaveQuote.setOnClickListener {
//            isSaved = !isSaved
//            val updatedIcon = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
//            viewBinding.imbSaveQuote.setImageResource(updatedIcon)
//
//            selectedQuote?.let { quote ->
//                dbHelper.updateQuoteSaveStatus(quote.quoteID, isSaved)
//            }
//        }
    }

}