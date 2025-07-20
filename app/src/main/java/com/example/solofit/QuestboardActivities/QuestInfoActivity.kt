package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.addOnTagsKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.descKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.difficultyKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.tagKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.titleKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.xpRewardKey
import com.example.solofit.R
import com.example.solofit.databinding.QuestInfoBinding
import android.view.View
import androidx.core.content.ContextCompat
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.statRewardKey


class QuestInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: QuestInfoBinding = QuestInfoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Get data from intent using correct types and defaults
        val title = this.intent.getStringExtra(titleKey) ?: ""
        val desc = this.intent.getStringExtra(descKey) ?: "No Description"
        val tag = this.intent.getStringExtra(tagKey) ?: ""
        val addOnTags = this.intent.getStringExtra(addOnTagsKey) ?: ""
        val difficulty = this.intent.getStringExtra(difficultyKey) ?: ""
        val xpReward = this.intent.getIntExtra(xpRewardKey, 0)
        val statReward = this.intent.getIntExtra(statRewardKey, 0)

        // Bind data to views
        viewBinding.txvQInfoQuestName.text = title
        viewBinding.txvQInfoDescription.text = desc
        val (mainIcon, extraIcon, statLabel) = when (tag) {
            "Strength" -> Triple(R.drawable.icon_str, R.drawable.icon_extra_str, "+ $statReward STR")
            "Endurance" -> Triple(R.drawable.icon_end, R.drawable.icon_extra_end, "+ $statReward END")
            "Vitality" -> Triple(R.drawable.icon_vit, R.drawable.icon_extra_vit, "+ $statReward VIT")
            else -> Triple(null, null, "")
        }

        mainIcon?.let { viewBinding.imvQInfoTagIcon.setImageResource(it) }

        if (addOnTags.isNotBlank() && extraIcon != null) {
            viewBinding.imvQInfoExtraTag.setImageResource(extraIcon)
            viewBinding.txvQInfoExtraTag.text = addOnTags
        } else {
            viewBinding.lloExtraTagRow.visibility = View.GONE
        }

        viewBinding.txvQInfoStatReward.text = statLabel



        val (bgRes, shadowColorRes) = when (difficulty) {
            "Easy" -> R.drawable.bg_diff_text_easy to R.color.bright_green
            "Normal" -> R.drawable.bg_diff_text_normal to R.color.cyan
            "Hard" -> R.drawable.bg_diff_text_hard to R.color.bright_red
            "Extreme" -> R.drawable.bg_diff_text_extreme to R.color.bright_purple
            else -> R.drawable.bg_diff_text_easy to R.color.bright_green // default fallback
        }
        viewBinding.txvQInfoDifficulty.setBackgroundResource(bgRes)
        viewBinding.txvQInfoDifficulty.setShadowLayer(10f, 0f, 0f, ContextCompat.getColor(this, shadowColorRes))
        viewBinding.txvQInfoDifficulty.text = difficulty
        viewBinding.txvQInfoExpReward.text = "+ $xpReward EXP"



        viewBinding.btnCancelQuest.setOnClickListener {
            val intentCancelQuest = Intent(applicationContext, QuestCancelledActivity::class.java)


            this.startActivity(intentCancelQuest)

        }

        viewBinding.btnCompleteQuest.setOnClickListener {
            val intentCompletedQuest = Intent(applicationContext, QuestCompletedActivity::class.java)


            this.startActivity(intentCompletedQuest)

        }


    }
}



