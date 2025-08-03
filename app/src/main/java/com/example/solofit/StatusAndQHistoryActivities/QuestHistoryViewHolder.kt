package com.example.solofit.StatusAndQHistoryActivities

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.Quest

class QuestHistoryViewHolder(val binding: QuestItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(quest: Quest) {
        binding.txvQuestName.text = quest.questName
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
            ContextCompat.getColor(binding.root.context, colorResId)
        )
    }

    fun setQuestNameTextColor(colorResId: Int) {
        binding.txvQuestName.setTextColor(
            ContextCompat.getColor(binding.root.context, colorResId)
        )
    }

    // Optional: keep this only if you still need it for log actions
    fun setActionBtnClickListenerAsLog(onClick: () -> Unit) {
        binding.imbQuestAction.setOnClickListener { onClick() }
    }

    fun setActionBtnVisibility(isVisible: Boolean) {
        binding.imbQuestAction.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setActionBtnIcon(iconResId: Int) {
        binding.imbQuestAction.setImageResource(iconResId)
    }
}
