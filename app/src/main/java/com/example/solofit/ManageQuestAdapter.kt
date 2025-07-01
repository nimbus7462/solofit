package com.example.solofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.EditableQuestBinding

class ManageQuestAdapter(private val quests: MutableList<Quest>) :
    RecyclerView.Adapter<ManageQuestAdapter.QuestViewHolder>() {

    inner class QuestViewHolder(val binding: EditableQuestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val binding = EditableQuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        val quest = quests[position]
        holder.binding.tvQuestTitle.text = quest.title
        holder.binding.ivIcon.setImageResource(quest.icon)

        // Set delete button listener
        holder.binding.ibDelete.setOnClickListener {
            quests.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, quests.size)
        }
    }

    override fun getItemCount(): Int = quests.size
}
