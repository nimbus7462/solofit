package com.example.solofit.QuoteActivities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.QuestboardActivities.QuestBoardViewHolder
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.databinding.SavedQuotesItemLayoutBinding
import com.example.solofit.model.Quote

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
        holder.setQuoteBackground(R.drawable.bg_quest_item_normal)
        holder.setQuotePreviewTextShadow(R.color.cyan)
        holder.setQuoteAuthorTextShadow(R.color.cyan)

    }

    override fun getItemCount(): Int = quotelist.size
}


