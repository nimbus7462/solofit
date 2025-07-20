package com.example.solofit.QuestboardActivities

import android.content.Intent
import com.example.solofit.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.databinding.AbortCompleteQuestBinding

class QuestAbortCompleteActivity : AppCompatActivity(){
    companion object {
        const val EXTRA_QUEST_STATUS = "quest_status"
        const val STATUS_COMPLETED = "completed"
        const val STATUS_CANCELLED = "cancelled"
        const val EXTRA_QUEST_NAME = "quest_name"
        const val EXTRA_XP_REWARD = "xp_reward"
        const val EXTRA_STAT_REWARD = "stat_reward"
        const val EXTRA_STAT_TYPE = "stat_type"
    }
    private lateinit var viewBinding: AbortCompleteQuestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = AbortCompleteQuestBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.txvQStatusQuestName.text = intent.getStringExtra(EXTRA_QUEST_NAME)

        val questStatus = intent.getStringExtra(EXTRA_QUEST_STATUS)

        when (questStatus) {
            STATUS_COMPLETED -> {
                viewBinding.lloQuestHeader.setBackgroundResource(R.drawable.bg_quest_complete)
                viewBinding.txvQuestStatusTitle.text = getString(R.string.quest_completed)
                viewBinding.btnFinishQuest.setBackgroundResource(R.drawable.bg_complete_btn)
                viewBinding.btnFinishQuest.text = getString(R.string.complete_quest)
                viewBinding.btnFinishQuest.setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_green))
                val expRewards = intent.getIntExtra(EXTRA_XP_REWARD, 0)
                viewBinding.txvExpChanges.text =  " ( +$expRewards ) EXP"
                viewBinding.txvExpChanges.setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_green))
                viewBinding.imvExpIcon.setImageResource(R.drawable.icon_exp_gain)

                val questStatType = intent.getStringExtra(EXTRA_STAT_TYPE)
                val statRewards = intent.getIntExtra(EXTRA_STAT_REWARD, 0)

                val statTextView = when (questStatType) {
                    "Strength" -> viewBinding.txvGainedStr
                    "Endurance" -> viewBinding.txvGainedEnd
                    "Vitality" -> viewBinding.txvGainedVit
                    else -> null
                }

                statTextView?.apply {
                    text = " ( +$statRewards )"
                    setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_green))
                }


            }

            STATUS_CANCELLED -> {
                viewBinding.lloQuestHeader.setBackgroundResource(R.drawable.bg_quest_abort)
                viewBinding.txvQuestStatusTitle.text = getString(R.string.quest_aborted)
                viewBinding.btnFinishQuest.setBackgroundResource(R.drawable.bg_return_btn)
                viewBinding.btnFinishQuest.text = getString(R.string.return_quest)
                viewBinding.btnFinishQuest.setShadowLayer(10f, 0f, 0f, getColor(R.color.light_gray))

                val expDeduction = intent.getIntExtra(EXTRA_XP_REWARD, 0) / 2
                viewBinding.txvExpChanges.text =  " ( -$expDeduction ) EXP"

                viewBinding.txvExpChanges.setShadowLayer(10f, 0f, 0f, getColor(R.color.bright_red))
                viewBinding.imvExpIcon.setImageResource(R.drawable.icon_exp_loss)


            }
        }


        viewBinding.btnLogThoughts.setOnClickListener {
            val intentLoggingActivity = Intent(applicationContext, QuestLoggingActivity::class.java)

            this.startActivity(intentLoggingActivity)
            finish()
        }

        viewBinding.btnFinishQuest.setOnClickListener {
            finish()
        }


        var isSaved = false

        viewBinding.imbSaveQuote.setOnClickListener {
            isSaved = !isSaved
            val newRes = if (isSaved) R.drawable.icon_saved_quote else R.drawable.icon_save_quote
            viewBinding.imbSaveQuote.setImageResource(newRes)
        }




    }
}