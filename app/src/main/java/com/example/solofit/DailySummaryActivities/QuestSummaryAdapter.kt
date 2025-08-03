package com.example.solofit.DailySummaryActivities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.R
import com.example.solofit.QuestInfoViewOnlyActivity
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestSummaryItemLayoutBinding
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras

class QuestSummaryAdapter(private val todaysUQAList: ArrayList<UserQuestActivity>, private val dbHelper: MyDatabaseHelper): Adapter<QuestSummaryViewHolder>(){

    private val reversedUQAList = todaysUQAList.asReversed()
    private val todaysQuestList = reversedUQAList.mapNotNull { dbHelper.getQuestById(it.questID) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestSummaryViewHolder {
        val itemViewBinding: QuestSummaryItemLayoutBinding = QuestSummaryItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestSummaryViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: QuestSummaryViewHolder, position: Int) {

        val uqaItem = reversedUQAList[position]
        val questItem = todaysQuestList[position]
        holder.bindData(questItem)

        holder.binding.lloQuestSumm.setOnClickListener {
            val intent = Intent(holder.itemView.context, QuestInfoViewOnlyActivity::class.java).apply {

                putExtra(Extras.EXTRA_QUEST_TITLE, questItem.questName)
                putExtra(Extras.EXTRA_DESC, questItem.description)
                putExtra(Extras.EXTRA_QUEST_TYPE, questItem.questType)
                putExtra(Extras.EXTRA_EXTRA_TAGS, questItem.extraTags)
                putExtra(Extras.EXTRA_DIFFICULTY, questItem.difficulty)
                putExtra(Extras.EXTRA_XP_REWARD, questItem.xpReward)
                putExtra(Extras.EXTRA_STAT_REWARD, questItem.statReward)


                putExtra(Extras.EXTRA_DATE_COMPLETED, uqaItem.dateCreated)
                putExtra(Extras.EXTRA_QUEST_STATUS, uqaItem.questStatus)
            }
            holder.itemView.context.startActivity(intent)

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

        val xpValue = questItem.xpReward.toString()
        val statValue = questItem.statReward.toString()
        val questType = questItem.questType.take(3).uppercase()
        when (uqaItem.questStatus) {
            "COMPLETED" -> {
                holder.binding.txvQSummExp.text = "+ $xpValue EXP"
                holder.binding.txvQSummStat.text = "+ $statValue $questType"
                holder.setExpAndStatTextDisplay(R.color.bright_green, true) // isCompleted
            }
            "ABORTED" -> {
                val xpPenalty = questItem.xpReward / 2
                holder.binding.txvQSummExp.text = "- $xpPenalty EXP"
                holder.setExpAndStatTextDisplay(R.color.bright_red, false) // isCompleted
            }
        }

    }


    override fun getItemCount(): Int {
        return todaysQuestList.size
    }

}