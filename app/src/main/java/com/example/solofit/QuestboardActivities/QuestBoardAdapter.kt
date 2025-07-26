package com.example.solofit.QuestboardActivities
import com.example.solofit.R

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.Quest
import com.example.solofit.utilities.Extras

class QuestBoardAdapter(private val questList: ArrayList<Quest>) : Adapter<QuestBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestBoardViewHolder {
        val itemViewBinding = QuestItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestBoardViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: QuestBoardViewHolder, position: Int) {
        val questItem = questList[position]
        holder.bindData(questItem)

        // Difficulty-based styling
        when (questItem.difficulty) {
            "Easy" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_easy)
                holder.setQuestNameTextShadow(R.color.bright_green)
            }
            "Normal" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_normal)
                holder.setQuestNameTextShadow(R.color.cyan)
            }
            "Hard" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_hard)
                holder.setQuestNameTextShadow(R.color.bright_red)
            }
            "Extreme" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_extreme)
                holder.setQuestNameTextShadow(R.color.bright_purple)
            }
        }

        // Icon by type
        when (questItem.questType) {
            "Strength" -> holder.setQuestIcon(R.drawable.icon_str)
            "Endurance" -> holder.setQuestIcon(R.drawable.icon_end)
            "Vitality" -> holder.setQuestIcon(R.drawable.icon_vit)
        }

        // Pass only the quest ID
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, QuestInfoActivity::class.java).apply {
                putExtra(Extras.QUEST_ID_KEY, questItem.id)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = questList.size
}

