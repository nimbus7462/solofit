package com.example.solofit.ManageQuestFragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.R
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.Quest

class ManageQuestAdapter(
    private val questList: MutableList<Quest>,
    private val onItemClick: (Quest) -> Unit,
    private val onDeleteClick: (Quest) -> Unit
) : RecyclerView.Adapter<ManageQuestViewHolder>() {

    fun updateList(newList: List<Quest>) {
        questList.clear()
        questList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageQuestViewHolder {
        val binding = QuestItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManageQuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManageQuestViewHolder, position: Int) {
        val questItem = questList[position]
        holder.bindData(questItem)

        holder.itemView.setOnClickListener {
            onItemClick(questItem)
        }

        // Switch case for background colors based on difficulty
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
        // Switch case for icon
        when (questItem.tag) {
            "Strength" -> holder.setQuestIcon(R.drawable.icon_str)
            "Endurance" -> holder.setQuestIcon(R.drawable.icon_end)
            "Vitality" -> holder.setQuestIcon(R.drawable.icon_vit)
        }

        holder.setActionBtnVisibility(isVisible = true)
        holder.setActionBtnIcon(R.drawable.icon_delete)
        holder.setActionBtnClickListenerAsDelete {
            onDeleteClick(questItem)
            questList.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int = questList.size
}
