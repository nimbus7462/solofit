package com.example.solofit.QuestboardActivities

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.Quest



class QuestBoardViewHolder(val binding: QuestItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(quest: Quest) {
        binding.txvQuestName.text = quest.title
    }

    fun setQuestIcon(iconResId: Int) {
        binding.imvQuestIcon.setImageResource(iconResId)
    }

    fun setQuestBackground(bgResId: Int) {
        binding.lloQuest.background = ContextCompat.getDrawable(binding.root.context, bgResId)
    }

    fun setQuestNameTextShadow(colorResId: Int) {
        binding.txvQuestName.setShadowLayer(
            10f,
            0f,
            0f,
            ContextCompat.getColor(binding.root.context, colorResId))
    }


}