package com.example.solofit.QuoteActivities
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.SavedQuotesItemLayoutBinding
import com.example.solofit.model.Quote

class QuoteViewHolder(private val binding: SavedQuotesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(quote: Quote) {
        val trimmedText = if (quote.quoteText.length > 15) {
            quote.quoteText.take(15).trimEnd() + "..."
        } else {
            quote.quoteText
        }

        binding.txvQuotePreview.text = trimmedText
        binding.txvAuthor.text = "-${quote.quoteAuthor}"
    }

    fun setQuoteBackground(bgResId: Int) {
        binding.lloQuoteSumm.background = ContextCompat.getDrawable(binding.root.context, bgResId)
    }

    fun setQuotePreviewTextShadow(colorResId: Int) {
        binding.txvQuotePreview.setShadowLayer(
            10f,
            0f,
            0f,
            ContextCompat.getColor(binding.root.context, colorResId)
        )
    }

    fun setQuoteAuthorTextShadow(colorResId: Int) {
        binding.txvAuthor.setShadowLayer(
            10f,
            0f,
            0f,
            ContextCompat.getColor(binding.root.context, colorResId)
        )
    }
}
