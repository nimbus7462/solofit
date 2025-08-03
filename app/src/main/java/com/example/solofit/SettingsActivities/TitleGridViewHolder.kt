package com.example.solofit.SettingsActivities

import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.R
import com.example.solofit.databinding.UserTitleItemLayoutBinding
import com.example.solofit.utilities.getTitleColorCategory
import com.example.solofit.utilities.getColorForCategory

class TitleGridViewHolder(val binding: UserTitleItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(title: String, isSelected: Boolean) {
        binding.txvTitle.text = title

        val context = itemView.context
        val category = getTitleColorCategory(title)
        val color = context.getColorForCategory(category)

        binding.txvTitle.setTextColor(color)
        binding.txvTitle.setShadowLayer(10f, 0f, 0f, color)

        binding.cloTitleConstraint.setBackgroundResource(
            if (isSelected) R.drawable.bg_selected_title else android.R.color.transparent
        )
    }
}
