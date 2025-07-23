package com.example.solofit.DailySummaryActivities

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.databinding.QuestSummaryItemLayoutBinding
import com.example.solofit.model.Quest

class QuestSummaryViewHolder(val binding: QuestSummaryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(quest: Quest) {
        binding.txvQSummQuestName.text = quest.title
    }

    fun setQuestIcon(iconResId: Int) {
        binding.imvQuestSummIcon.setImageResource(iconResId)
    }

    fun setQuestBackground(bgResId: Int) {
        binding.lloQuestSumm.background = ContextCompat.getDrawable(binding.root.context, bgResId)
    }
    fun setQuestNameTextShadow(colorResId: Int) {
        binding.txvQSummQuestName.setShadowLayer(
            10f,
            0f,
            0f,
            ContextCompat.getColor(binding.root.context, colorResId))
    }
    fun setExpAndStatTextDisplay(colorResId: Int, isQuestCompleted : Boolean) {
        binding.txvQSummExp.setShadowLayer(
            10f,
            0f,
            0f,
            ContextCompat.getColor(binding.root.context, colorResId))
        if(isQuestCompleted) {
            binding.txvQSummStat.setShadowLayer(
                10f,
                0f,
                0f,
                ContextCompat.getColor(binding.root.context, colorResId))
        } else {
            binding.txvQSummStat.visibility = View.GONE
        }
    }
}