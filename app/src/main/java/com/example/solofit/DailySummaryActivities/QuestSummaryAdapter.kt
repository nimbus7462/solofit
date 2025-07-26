package com.example.solofit.DailySummaryActivities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.R
import com.example.solofit.StatusAndQHistoryActivities.EditLogActivity
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_ADD_ON_TAGS
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_DATE_COMPLETED
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_DESC
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_DIFFICULTY
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_QUEST_STATUS
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_QUEST_TITLE
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_STAT_REWARD
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_TAG
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_UQA_ID
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_USER_LOG
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter.Companion.EXTRA_XP_REWARD
import com.example.solofit.QuestInfoViewOnlyActivity
import com.example.solofit.databinding.QuestSummaryItemLayoutBinding
import com.example.solofit.model.Quest
import com.example.solofit.model.UserQuestActivity

class QuestSummaryAdapter(private val questList: ArrayList<Quest>, private val userQuestActList: ArrayList<UserQuestActivity>): Adapter<QuestSummaryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestSummaryViewHolder {
        val itemViewBinding: QuestSummaryItemLayoutBinding = QuestSummaryItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestSummaryViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: QuestSummaryViewHolder, position: Int) {
        val questItem = questList[position]
        holder.bindData(questItem)

        holder.binding.lloQuestSumm.setOnClickListener {
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
        when (questItem.questType) {
            "Strength" -> holder.setQuestIcon(R.drawable.icon_str)
            "Endurance" -> holder.setQuestIcon(R.drawable.icon_end)
            "Vitality" -> holder.setQuestIcon(R.drawable.icon_vit)
        }

        val matchingUQA = userQuestActList.find { it.questID == questItem.id }

        matchingUQA?.let {
            val xpValue = questItem.xpReward.toString()
            val statValue = questItem.statReward.toString()
            val questType = questItem.questType.take(3).uppercase()
            when (it.questStatus) {
                "Completed" -> {
                    holder.binding.txvQSummExp.text = "+ $xpValue EXP"
                    holder.binding.txvQSummStat.text = "+ $statValue $questType"
                    holder.setExpAndStatTextDisplay(R.color.bright_green, true) // isCompleted
                }
                "Aborted" -> {
                    val xpPenalty = questItem.xpReward / 2
                    holder.binding.txvQSummExp.text = "- $xpPenalty EXP"
                    holder.setExpAndStatTextDisplay(R.color.bright_red, false) // isCompleted
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return questList.size
    }

}