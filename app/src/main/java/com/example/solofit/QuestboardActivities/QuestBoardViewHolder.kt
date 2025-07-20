package com.example.solofit.QuestboardActivities

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.model.Quest
import com.example.solofit.databinding.QuestboardItemLayoutBinding


class QuestBoardViewHolder(val binding: QuestboardItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(quest: Quest) {
        binding.txvQuestName.text = quest.title
    }

    fun setQuestIcon(iconResId: Int) {
        binding.imvQuestIcon.setImageResource(iconResId)
    }

    fun setQuestBackground(bgResId: Int) {
        binding.lloQuest.background = ContextCompat.getDrawable(binding.root.context, bgResId)
    }

}