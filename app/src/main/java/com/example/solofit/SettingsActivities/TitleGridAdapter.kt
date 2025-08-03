package com.example.solofit.SettingsActivities
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.solofit.R
import com.example.solofit.databinding.UserTitleItemLayoutBinding

class TitleGridAdapter(
    private val titles: List<String>,
    private var selectedTitle: String?,
    private val onSelect: (String) -> Unit
) : RecyclerView.Adapter<TitleGridAdapter.TitleViewHolder>() {

    inner class TitleViewHolder(val binding: UserTitleItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val binding = UserTitleItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val title = titles[position]
        holder.binding.txvTitle.text = title

        val isSelected = title == selectedTitle
        holder.binding.cloTitleConstraint.setBackgroundResource(
            if (isSelected) R.drawable.bg_selected_title else android.R.color.transparent
        )

        holder.binding.root.setOnClickListener {
            val previousSelected = selectedTitle
            selectedTitle = title
            onSelect(title)

            // Update UI: only notify previously selected + newly selected items
            notifyItemChanged(titles.indexOf(previousSelected))
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = titles.size
}
