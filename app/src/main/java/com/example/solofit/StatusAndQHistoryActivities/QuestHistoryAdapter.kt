package com.example.solofit.StatusAndQHistoryActivities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.QuestInfoViewOnlyActivity
import com.example.solofit.R
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.Quest
import com.example.solofit.model.UserQuestActivity

class QuestHistoryAdapter(private val questList: ArrayList<Quest>, private val userQuestActList: ArrayList<UserQuestActivity>): Adapter<QuestHistoryViewHolder>() {

    companion object {
        const val EXTRA_QUEST_TITLE = "QUEST_TITLE_KEY"
        const val EXTRA_DATE_COMPLETED = "DATE_COMPLETED_KEY"
        const val EXTRA_USER_LOG = "USER_LOG_KEY"
        const val EXTRA_QUEST_STATUS = "QUEST_STATUS_KEY"
        const val EXTRA_UQA_ID = "USER_QUEST_ACT"

        // Moved from QuestBoardAdapter
        const val EXTRA_TAG = "QUEST_TAG_KEY"
        const val EXTRA_ADD_ON_TAGS = "QUEST_ADD_ON_TAGS_KEY"
        const val EXTRA_DESC = "QUEST_DESC_KEY"
        const val EXTRA_DIFFICULTY = "QUEST_DIFFICULTY_KEY"
        const val EXTRA_XP_REWARD = "QUEST_XP_REWARD_KEY"
        const val EXTRA_STAT_REWARD = "QUEST_STAT_REWARD_KEY"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestHistoryViewHolder {
        val itemViewBinding: QuestItemLayoutBinding = QuestItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestHistoryViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: QuestHistoryViewHolder, position: Int) {
        val questItem = questList[position]
        holder.bindData(questItem)
        holder.setActionBtnIcon(R.drawable.icon_log)
        holder.setActionBtnVisibility(isVisible = true)
        holder.itemView.setOnClickListener {
            // Find the UQA first, then match the quest to it
            val matchingUQA = userQuestActList.find { it.questID == questItem.id }

            matchingUQA?.let { uqa ->
                val matchingQuest = questList.find { it.id == uqa.questID }

                matchingQuest?.let { questItem ->
                    val intent = Intent(holder.itemView.context, QuestInfoViewOnlyActivity::class.java).apply {
                        // From Quest
                        putExtra(EXTRA_QUEST_TITLE, questItem.questName)
                        putExtra(EXTRA_DESC, questItem.description)
                        putExtra(EXTRA_TAG, questItem.questType)
                        putExtra(EXTRA_ADD_ON_TAGS, questItem.extraTags)
                        putExtra(EXTRA_DIFFICULTY, questItem.difficulty)
                        putExtra(EXTRA_XP_REWARD, questItem.xpReward)
                        putExtra(EXTRA_STAT_REWARD, questItem.statReward)


                        // From UQA
                        putExtra(EXTRA_DATE_COMPLETED, uqa.dateCreated)
                        putExtra(EXTRA_QUEST_STATUS, uqa.questStatus)
                    }
                    holder.itemView.context.startActivity(intent)
                }
            }

        }

        // Set icon based on tag
        when (questItem.questType) {
            "Strength" -> holder.setQuestIcon(R.drawable.icon_str)
            "Endurance" -> holder.setQuestIcon(R.drawable.icon_end)
            "Vitality" -> holder.setQuestIcon(R.drawable.icon_vit)
        }


        val matchingUQA = userQuestActList.find { it.questID == questItem.id }

        matchingUQA?.let {
            when (it.questStatus) {
                "Completed" -> {
                    holder.setQuestBackground(R.drawable.bg_qhist_completed)
                    holder.setQuestNameTextShadow(R.color.bright_green)
                }
                "Aborted" -> {
                    holder.setQuestBackground(R.drawable.bg_qhist_aborted)
                    holder.setQuestNameTextShadow(R.color.light_gray)
                }
            }
        }

        holder.setActionBtnClickListenerAsLog {
            // Match UserQuestActivity by questID
            val matchingUQA = userQuestActList.find { it.questID == questItem.id }

            // Only proceed if there's a matching UQA
            matchingUQA?.let { uqa ->
                val intent = Intent(holder.itemView.context, EditLogActivity::class.java).apply {
                    putExtra(EXTRA_QUEST_TITLE, questItem.questName)
                    putExtra(EXTRA_DATE_COMPLETED, uqa.dateCreated)
                    putExtra(EXTRA_USER_LOG, uqa.userLogs)
                    putExtra(EXTRA_QUEST_STATUS, uqa.questStatus)
                    putExtra(EXTRA_UQA_ID, uqa.userQuestActID)
                }
                holder.itemView.context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return questList.size
    }

}