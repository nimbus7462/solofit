package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.model.Quest
import com.example.solofit.databinding.QuestboardItemLayoutBinding

/*
data class Quest(
    val title: String,
    val description: String,
    val tag: String,           // Strength, Endurance, Vitality
    val addOnTags: String,
    val difficulty: String,    // Easy, Medium, Hard
    val xpReward: Int,
    val isCompleted: Boolean,
    val isCancelled: Boolean,
    val icon: Int
)
 */

class QuestBoardAdapter(private val questList: ArrayList<Quest>): Adapter<QuestBoardViewholder>() {

    // These are static keys that you can use for the passing data around.
    companion object {
        const val titleKey : String = "TITLE_KEY"
        const val descKey : String = "DESC_KEY"
        const val tagKey : String = "TAG_KEY"
        const val addOnTagsKey : String = "ADD_ON_TAGS_KEY"
        const val difficultyKey: String = "DIFFICULTY_KEY"
        const val xpRewardKey: String = "XP_REWARD_KEY"
        const val iconKey: String = "ICON_KEY"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestBoardViewholder {
        val itemViewBinding: QuestboardItemLayoutBinding = QuestboardItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestBoardViewholder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: QuestBoardViewholder, position: Int) {
        val questItem = questList[position]
        holder.bindData(questItem)

        // Set up click listener on the whole itemView
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, QuestInfoActivity::class.java).apply {
                putExtra(titleKey, questItem.title)
                putExtra(descKey, questItem.description)
                putExtra(tagKey, questItem.tag)
                putExtra(addOnTagsKey, questItem.addOnTags)
                putExtra(difficultyKey, questItem.difficulty)
                putExtra(xpRewardKey, questItem.xpReward)
                putExtra(iconKey, questItem.icon)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return questList.size
    }

}
