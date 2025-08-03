package com.example.solofit.QuoteActivities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.ViewSavedQuoteBinding
import com.example.solofit.model.Quote
import com.example.solofit.utilities.Extras

class ViewSavedQuote : AppCompatActivity(){
    companion object {
        const val CONFIRM_DELETE_MESSAGE = "Are you sure you want to delete this from your quotes?"
        const val YES = "Yes"
        const val NO = "No"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ViewSavedQuoteBinding = ViewSavedQuoteBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val quoteId = intent.getIntExtra(Extras.QUOTE_ID, -1)
        if (quoteId == -1) {
            finish()
            return
        }
        val dbHelper = MyDatabaseHelper.getInstance(this)!!
        val quote: Quote = dbHelper?.getQuoteById(quoteId) ?: run {
            finish() // Quote not found
            return
        }
        viewBinding.tvQuoteContent.text = quote.quoteText
        viewBinding.tvQuoteAuthor.text = quote.quoteAuthor
        viewBinding.ivSaveQuote.setOnClickListener {
            viewBinding.txvConfirmationMsg.text = CONFIRM_DELETE_MESSAGE
            viewBinding.btnConfirm.text = YES
            viewBinding.btnGoBack.text = NO
            handleConfirmation(viewBinding)
        }
        viewBinding.btnConfirm.setOnClickListener {
            quote.isSaved = !quote.isSaved
            dbHelper.updateQuote(quote)
            finish()
        }
        viewBinding.btnGoBack.setOnClickListener {
            hidePanel(viewBinding)
        }
    }
    private fun handleConfirmation(viewBinding: ViewSavedQuoteBinding) {
        viewBinding.cloConfirmation.visibility = View.VISIBLE
        viewBinding.viewBackgroundBlocker.visibility = View.VISIBLE
    }
    private fun hidePanel(viewBinding: ViewSavedQuoteBinding)
    {
        viewBinding.cloConfirmation.visibility = View.INVISIBLE
        viewBinding.viewBackgroundBlocker.visibility = View.INVISIBLE
    }

}