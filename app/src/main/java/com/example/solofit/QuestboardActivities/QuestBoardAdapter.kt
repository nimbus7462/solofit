package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestItemLayoutBinding
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras

class QuestBoardAdapter(
    private var uqaList: List<UserQuestActivity>,
    private val dbHelper: MyDatabaseHelper
) : RecyclerView.Adapter<QuestBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestBoardViewHolder {
        val binding = QuestItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestBoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestBoardViewHolder, position: Int) {
        val uqaItem = uqaList[position]
        val questItem = dbHelper.getQuestById(uqaItem.questID) ?: return

        holder.bindData(questItem)
        holder.setQuestNameTextColor(R.color.white) // Set text color to white

        when (questItem.difficulty) {
            "Easy" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_easy)
                holder.setQuestNameTextShadow(R.color.bright_green)
            }
            "Normal" -> {
                holder.setQuestBackground(R.drawable.bg_quest_item_normal)
                holder.setQuestNameTextShadow(R.color.orange)
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

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, QuestInfoActivity::class.java)
            intent.putExtra(Extras.EXTRA_UQA, uqaItem)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = uqaList.size

    fun updateData(newUqaList: List<UserQuestActivity>) {
        uqaList = newUqaList
        notifyDataSetChanged()
    }
}
