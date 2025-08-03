package com.example.solofit.SettingsActivities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.databinding.UserTitleItemLayoutBinding

class TitleGridAdapter(
    private val titles: List<String>,
    private var selectedTitle: String?,
    private val onSelect: (String) -> Unit
) : RecyclerView.Adapter<TitleGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleGridViewHolder {
        val binding = UserTitleItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TitleGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TitleGridViewHolder, position: Int) {
        val title = titles[position]
        val isSelected = title == selectedTitle

        holder.bind(title, isSelected)

        holder.binding.root.setOnClickListener {
            val previousSelected = selectedTitle
            selectedTitle = title
            onSelect(title)

            notifyItemChanged(titles.indexOf(previousSelected))
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = titles.size
}
