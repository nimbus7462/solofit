package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.solofit.R
import com.example.solofit.database.MyDatabaseHelper
import com.example.solofit.databinding.QuestInfoBinding
import com.example.solofit.model.Quest
import com.example.solofit.model.UserQuestActivity
import com.example.solofit.utilities.Extras

class QuestInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: QuestInfoBinding = QuestInfoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val todaysSelectedUQA = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                intent.getParcelableExtra(Extras.EXTRA_UQA, UserQuestActivity::class.java)
            else ->
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Extras.EXTRA_UQA)
        } ?: UserQuestActivity()

        if (todaysSelectedUQA.questID == -1) {
            finish() // invalid id, exit early
            return
        }

        val dbHelper = MyDatabaseHelper.getInstance(this)!!
        val quest: Quest = dbHelper.getQuestById(todaysSelectedUQA.questID) ?: run {
            finish() // quest not found, exit early
            return
        }

        // Display data
        viewBinding.txvQInfoQuestName.text = quest.questName
        viewBinding.txvQInfoDescription.text = quest.description

        val (mainIcon, extraIcon, statLabel) = when (quest.questType) {
            "Strength" -> Triple(R.drawable.icon_str, R.drawable.icon_extra_str, "+ ${quest.statReward} STR")
            "Endurance" -> Triple(R.drawable.icon_end, R.drawable.icon_extra_end, "+ ${quest.statReward} END")
            "Vitality" -> Triple(R.drawable.icon_vit, R.drawable.icon_extra_vit, "+ ${quest.statReward} VIT")
            else -> Triple(null, null, "")
        }

        mainIcon?.let { viewBinding.imvQInfoTagIcon.setImageResource(it) }

        if (quest.extraTags.isNotBlank() && extraIcon != null) {
            viewBinding.imvQInfoExtraTag.setImageResource(extraIcon)
            viewBinding.txvQInfoExtraTag.text = quest.extraTags
        } else {
            viewBinding.lloExtraTagRow.visibility = View.GONE
        }

        viewBinding.txvQInfoStatReward.text = statLabel
        viewBinding.txvQInfoExpReward.text = "+ ${quest.xpReward} EXP"

        val (bgRes, shadowColorRes) = when (quest.difficulty) {
            "Easy" -> R.drawable.bg_diff_text_easy to R.color.bright_green
            "Normal" -> R.drawable.bg_diff_text_normal to R.color.cyan
            "Hard" -> R.drawable.bg_diff_text_hard to R.color.bright_red
            "Extreme" -> R.drawable.bg_diff_text_extreme to R.color.bright_purple
            else -> R.drawable.bg_diff_text_easy to R.color.bright_green
        }
        viewBinding.txvQInfoDifficulty.setBackgroundResource(bgRes)
        viewBinding.txvQInfoDifficulty.setShadowLayer(10f, 0f, 0f, ContextCompat.getColor(this, shadowColorRes))
        viewBinding.txvQInfoDifficulty.text = quest.difficulty

        // Button: Abort Quest
        viewBinding.btnAbortQuest.setOnClickListener {
            val intentAbortQuest = Intent(applicationContext, QuestAbortCompleteActivity::class.java).apply {
                putExtra(Extras.EXTRA_QUEST_STATUS, Extras.STATUS_ABORTED)
                putExtra(Extras.EXTRA_UQA, todaysSelectedUQA)
            }
            startActivity(intentAbortQuest)
            finish()
        }

        // Button: Complete Quest
        viewBinding.btnCompleteQuest.setOnClickListener {
            val intentCompletedQuest = Intent(applicationContext, QuestAbortCompleteActivity::class.java).apply {
                putExtra(Extras.EXTRA_QUEST_STATUS, Extras.STATUS_COMPLETED)
                putExtra(Extras.EXTRA_UQA, todaysSelectedUQA)
            }
            startActivity(intentCompletedQuest)
            finish()
        }
    }
}
