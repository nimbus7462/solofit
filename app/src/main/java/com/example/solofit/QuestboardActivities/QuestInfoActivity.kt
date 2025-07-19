package com.example.solofit.QuestboardActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.addOnTagsKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.descKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.difficultyKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.iconKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.tagKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.titleKey
import com.example.solofit.QuestboardActivities.QuestBoardAdapter.Companion.xpRewardKey
import com.example.solofit.R
import com.example.solofit.databinding.QuestInfoBinding

class QuestInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: QuestInfoBinding = QuestInfoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Get data from intent using correct types and defaults
        val title = this.intent.getStringExtra(titleKey) ?: "No Title"
        val desc = this.intent.getStringExtra(descKey) ?: "No Description"
        val tag = this.intent.getStringExtra(tagKey) ?: "None"
        val addOnTags = this.intent.getStringExtra(addOnTagsKey) ?: ""
        val difficulty = this.intent.getStringExtra(difficultyKey) ?: "Unknown"
        val xpReward = this.intent.getIntExtra(xpRewardKey, 0)
        val iconResId = this.intent.getIntExtra(iconKey, R.drawable.icon_muscle)

        // Bind data to views
        viewBinding.questTitle.text = title
        viewBinding.questInstructions.text = desc
        viewBinding.questTags.text = listOf(tag, addOnTags).filter { it.isNotBlank() }.joinToString(", ")
        viewBinding.valueDifficulty.text = difficulty
        viewBinding.textExpGain.text = "+$xpReward EXP"
        // viewBinding.titleImage.setImageResource(iconResId)


        viewBinding.btnCancel.setOnClickListener {
            val intentCancelQuest = Intent(applicationContext, QuestCancelledActivity::class.java)


            this.startActivity(intentCancelQuest)

        }

        viewBinding.btnComplete.setOnClickListener {
            val intentCompletedQuest = Intent(applicationContext, QuestCompletedActivity::class.java)


            this.startActivity(intentCompletedQuest)

        }


    }
}



