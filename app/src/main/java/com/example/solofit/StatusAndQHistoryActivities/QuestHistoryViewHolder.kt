package com.example.solofit.StatusAndQHistoryActivities

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestHistoryItemLayoutBinding
import com.example.solofit.model.Quest

class QuestHistoryViewHolder(val binding: QuestHistoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindData(quest: Quest) {
        binding.txvQHistQuestName.text = quest.title
    }

    fun setQuestIcon(iconResId: Int) {
        binding.imvQHistQuestIcon.setImageResource(iconResId)
    }

    fun setQuestBackground(bgResId: Int) {
        binding.lloQuestHist.background = ContextCompat.getDrawable(binding.root.context, bgResId)
    }

    fun setLogBtnClickListener(onClick: () -> Unit) {
        binding.imbLogBtn.setOnClickListener { onClick() }
    }

}