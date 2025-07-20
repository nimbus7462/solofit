package com.example.solofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.EditableQuestBinding
import com.example.solofit.model.Quest

class ManageQuestAdapter(
    private val quests: MutableList<Quest>,
    private val onItemClick: (Quest) -> Unit
) : RecyclerView.Adapter<ManageQuestAdapter.QuestViewHolder>() {

    fun updateList(newList: List<Quest>) {
        quests.clear()
        quests.addAll(newList)
        notifyDataSetChanged()
    }

    inner class QuestViewHolder(val binding: EditableQuestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val binding = EditableQuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        val quest = quests[position]
        holder.binding.tvQuestTitle.text = quest.title
       // holder.binding.ivIcon.setImageResource(quest.icon)
        holder.binding.imageButton2.setOnClickListener {
            onItemClick(quest)
        }
        holder.binding.ibDelete.setOnClickListener {
            val questToRemove = quests[position]

            // Remove from the data source
            QuestDataHelper.quests.removeIf { it.id == questToRemove.id }

            // Remove from the adapter list
            quests.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = quests.size
}
