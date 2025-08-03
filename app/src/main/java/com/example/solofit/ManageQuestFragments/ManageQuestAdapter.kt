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

    // Full list of quests to allow filtering
    private val fullQuestList = mutableListOf<Quest>()

    // Filters
    private var currentTypeFilter: String = "All"
    private var currentDifficultyFilter: String = "All"

    fun updateList(newList: List<Quest>) {
        fullQuestList.clear()
        fullQuestList.addAll(newList)

        // Apply filters immediately
        val filteredList = fullQuestList.filter { quest ->
            (currentTypeFilter == "All" || quest.questType == currentTypeFilter) &&
                    (currentDifficultyFilter == "All" || quest.difficulty == currentDifficultyFilter)
        }

        questList.clear()
        questList.addAll(filteredList)
        notifyDataSetChanged()
    }

    fun applyFilters(type: String, difficulty: String) {
        currentTypeFilter = type
        currentDifficultyFilter = difficulty

        val filteredList = fullQuestList.filter { quest ->
            (type == "All" || quest.questType == type) &&
                    (difficulty == "All" || quest.difficulty == difficulty)
        }

        questList.clear()
        questList.addAll(filteredList)
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

        when (questItem.questType) {
            "Strength" -> holder.setQuestIcon(R.drawable.icon_str)
            "Endurance" -> holder.setQuestIcon(R.drawable.icon_end)
            "Vitality" -> holder.setQuestIcon(R.drawable.icon_vit)
        }

        holder.setQuestNameTextColor(R.color.white)

        holder.setActionBtnVisibility(true)
        holder.setActionBtnIcon(R.drawable.icon_delete)
        holder.setActionBtnClickListenerAsDelete {
            onDeleteClick(questItem)
        }
    }

    override fun getItemCount(): Int = questList.size
}
