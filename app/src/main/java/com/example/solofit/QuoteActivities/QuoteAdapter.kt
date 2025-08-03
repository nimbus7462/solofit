package com.example.solofit.QuoteActivities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.SavedQuotesItemLayoutBinding
import com.example.solofit.model.Quote
import com.example.solofit.utilities.Extras

class QuoteAdapter (private val quotelist: ArrayList<Quote>, private val dbHelper: MyDatabaseHelper): Adapter<QuoteViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val itemViewBinding = SavedQuotesItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuoteViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quoteItemList = quotelist[position]
        holder.bindData(quoteItemList)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ViewSavedQuote::class.java).apply {
                putExtra(Extras.QUOTE_ID, quoteItemList.quoteID)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = quotelist.size
}


