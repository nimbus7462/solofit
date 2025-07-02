package com.example.solofit

import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestboardItemLayoutBinding


class QuestBoardViewholder(val binding: QuestboardItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(quest: Quest) {
        binding.txvQuestName.text = quest.title
        binding.imvQuestIcon.setImageResource(quest.icon)
    }

}