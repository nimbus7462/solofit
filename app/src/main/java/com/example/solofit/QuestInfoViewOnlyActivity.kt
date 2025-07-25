package com.example.solofit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.solofit.StatusAndQHistoryActivities.QuestHistoryAdapter
import com.example.solofit.databinding.QuestInfoBinding
import com.example.solofit.utilities.Extras

class QuestInfoViewOnlyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: QuestInfoBinding = QuestInfoBinding.inflate(layoutInflater)
        viewBinding.lloQuestInfoBtnRow.visibility = View.GONE
        viewBinding.lloShowIfQHistRow.visibility = View.VISIBLE
        setContentView(viewBinding.root)

// Get data from intent using correct types and defaults
        val title = intent.getStringExtra(Extras.EXTRA_QUEST_TITLE) ?: ""
        val desc = intent.getStringExtra(Extras.EXTRA_DESC) ?: "No Description"
        val questType = intent.getStringExtra(Extras.EXTRA_QUEST_TYPE) ?: ""
        val extraTags = intent.getStringExtra(Extras.EXTRA_EXTRA_TAGS) ?: ""
        val difficulty = intent.getStringExtra(Extras.EXTRA_DIFFICULTY) ?: ""
        val xpReward = intent.getIntExtra(Extras.EXTRA_XP_REWARD, 0)
        val statReward = intent.getIntExtra(Extras.EXTRA_STAT_REWARD, 0)


        // Bind data to views
        viewBinding.txvQInfoQuestName.text = title
        viewBinding.txvQInfoDescription.text = desc
        val (mainIcon, extraIcon, statLabel) = when (questType) {
            "Strength" -> Triple(
                R.drawable.icon_str,
                R.drawable.icon_extra_str,
                "+ $statReward STR"
            )

            "Endurance" -> Triple(
                R.drawable.icon_end,
                R.drawable.icon_extra_end,
                "+ $statReward END"
            )

            "Vitality" -> Triple(
                R.drawable.icon_vit,
                R.drawable.icon_extra_vit,
                "+ $statReward VIT"
            )

            else -> Triple(null, null, "")
        }

        mainIcon?.let { viewBinding.imvQInfoTagIcon.setImageResource(it) }

        if (extraTags.isNotBlank() && extraIcon != null) {
            viewBinding.imvQInfoExtraTag.setImageResource(extraIcon)
            viewBinding.txvQInfoExtraTag.text = extraTags
        } else {
            viewBinding.lloExtraTagRow.visibility = View.GONE
        }

        viewBinding.txvQInfoStatReward.text = statLabel


        // setting the difficulty color schemes
        val (bgRes, shadowColorRes) = when (difficulty) {
            "Easy" -> R.drawable.bg_diff_text_easy to R.color.bright_green
            "Normal" -> R.drawable.bg_diff_text_normal to R.color.cyan
            "Hard" -> R.drawable.bg_diff_text_hard to R.color.bright_red
            "Extreme" -> R.drawable.bg_diff_text_extreme to R.color.bright_purple
            else -> R.drawable.bg_diff_text_easy to R.color.bright_green // default fallback
        }
        viewBinding.txvQInfoDifficulty.setBackgroundResource(bgRes)
        viewBinding.txvQInfoDifficulty.setShadowLayer(
            10f,
            0f,
            0f,
            ContextCompat.getColor(this, shadowColorRes)
        )
        viewBinding.txvQInfoDifficulty.text = difficulty

        val questStatus = intent.getStringExtra(Extras.EXTRA_QUEST_STATUS)
        val statusText = when (questStatus) {
            "ABORTED" -> getString(R.string.aborted)
            "COMPLETED" -> getString(R.string.completed)
            else -> {"INVALID"}
        }


        // date
        val dateCompleted = intent.getStringExtra(Extras.EXTRA_DATE_COMPLETED)
        viewBinding.txvQInfoCompletedDate.text = dateCompleted
        viewBinding.txvQInfoQuestStatus.text = statusText


        viewBinding.txvQInfoExpReward.text = "+ $xpReward EXP"

    }
}