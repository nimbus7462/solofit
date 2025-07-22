package com.example.solofit.QuestboardActivities
import com.example.solofit.R

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.Quest

class QuestBoardAdapter(private val questList: ArrayList<Quest>): Adapter<QuestBoardViewHolder>() {

    // These are static keys that you can use for the passing data around.
    companion object {
        const val titleKey : String = "TITLE_KEY"
        const val descKey : String = "DESC_KEY"
        const val tagKey : String = "TAG_KEY"
        const val addOnTagsKey : String = "ADD_ON_TAGS_KEY"
        const val difficultyKey: String = "DIFFICULTY_KEY"
        const val xpRewardKey: String = "XP_REWARD_KEY"
        const val statRewardKey: String = "STAT_REWARD_KEY"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestBoardViewHolder {
        val itemViewBinding: QuestItemLayoutBinding = QuestItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestBoardViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: QuestBoardViewHolder, position: Int) {
        val questItem = questList[position]
        holder.bindData(questItem)

        // Switch case for background colors based on difficulty
        when (questItem.difficulty) {
            "Easy" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_easy)
            }
            "Normal" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_normal)
            }
            "Hard" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_hard)
            }
            "Extreme" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_extreme)
            }
        }

        // Switch case for icon
        when(questItem.tag) {
            "Strength" -> {
                holder.setQuestIcon(R.drawable.icon_str)
            }
            "Endurance" -> {
                holder.setQuestIcon(R.drawable.icon_end)
            }
            "Vitality" -> {
                holder.setQuestIcon(R.drawable.icon_vit)
            }
        }

        // Set up click listener on the whole itemView
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, QuestInfoActivity::class.java).apply {
                putExtra(titleKey, questItem.title)
                putExtra(descKey, questItem.description)
                putExtra(tagKey, questItem.tag)
                putExtra(addOnTagsKey, questItem.addOnTags)
                putExtra(difficultyKey, questItem.difficulty)
                putExtra(xpRewardKey, questItem.xpReward)
                putExtra(statRewardKey, questItem.statReward)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return questList.size
    }

}
