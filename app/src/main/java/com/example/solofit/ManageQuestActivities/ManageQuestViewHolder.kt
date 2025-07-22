package com.example.solofit.ManageQuestActivities

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.Quest

class ManageQuestViewHolder(val binding: QuestItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(quest: Quest) {
        binding.txvQuestName.text = quest.title
    }

    fun setQuestIcon(iconResId: Int) {
        binding.imvQuestIcon.setImageResource(iconResId)
    }

    fun setQuestBackground(bgResId: Int) {
        binding.lloQuest.background = ContextCompat.getDrawable(binding.root.context, bgResId)
    }

    fun setActionBtnClickListenerAsDelete(onClick: () -> Unit) {
        binding.imbQuestAction.setOnClickListener { onClick() }
    }


    fun setActionBtnVisibility(isVisible: Boolean) {
        binding.imbQuestAction.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun setActionBtnIcon(iconResId: Int) {
        binding.imbQuestAction.setImageResource(iconResId)
    }

}