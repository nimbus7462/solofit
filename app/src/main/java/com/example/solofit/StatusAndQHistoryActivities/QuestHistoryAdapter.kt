package com.example.solofit.StatusAndQHistoryActivities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.solofit.QuestInfoViewOnlyActivity
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras

class QuestHistoryAdapter(
    private var uqaList: List<UserQuestActivity>,
    private val dbHelper: MyDatabaseHelper
) : RecyclerView.Adapter<QuestHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestHistoryViewHolder {
        val binding = QuestItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestHistoryViewHolder, position: Int) {
        val uqaItem = uqaList[position]
        val questItem = dbHelper.getQuestById(uqaItem.questID) ?: return

        holder.bindData(questItem)
        holder.setActionBtnIcon(R.drawable.icon_log)
        holder.setActionBtnVisibility(true)

        when (questItem.questType) {
            "Strength" -> holder.setQuestIcon(R.drawable.icon_str)
            "Endurance" -> holder.setQuestIcon(R.drawable.icon_end)
            "Vitality" -> holder.setQuestIcon(R.drawable.icon_vit)
        }

        when (uqaItem.questStatus) {
            "COMPLETED" -> {
                holder.setQuestBackground(R.drawable.bg_qhist_completed)
                holder.setQuestNameTextShadow(R.color.bright_green)
            }
            "ABORTED" -> {
                holder.setQuestBackground(R.drawable.bg_qhist_aborted)
                holder.setQuestNameTextShadow(R.color.light_gray)
            }
        }

        holder.setActionBtnClickListenerAsLog {
            val intent = Intent(holder.itemView.context, EditLogActivity::class.java).apply {
                putExtra(Extras.EXTRA_QUEST_TITLE, questItem.questName)
                putExtra(Extras.EXTRA_DATE_COMPLETED, uqaItem.dateCreated)
                putExtra(Extras.EXTRA_UQA_ID, uqaItem.userQuestActID)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
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
    }

    override fun getItemCount(): Int = uqaList.size

    fun updateData(newUqaList: List<UserQuestActivity>) {
        uqaList = newUqaList
        notifyDataSetChanged()
    }
}