package com.example.solofit.datahelpers

import com.example.solofit.model.Quest

class QuestDataHelper {

    companion object {
        val quests: MutableList<Quest> = mutableListOf()

        fun addQuest(quest: Quest) {
            quests.add(quest)
        }

        fun updateQuest(index: Int, quest: Quest) {
            quests[index] = quest
        }

        fun getQuestsFromUserQuestActivities(): ArrayList<Quest> {
            val userQuestActs = UserQuestActivityDataHelper.initUQA()
            val allQuests = initializeQuests()

            val joinedQuests = userQuestActs.mapNotNull { uqa ->
                allQuests.find { it.id == uqa.questID }
            }

            return ArrayList(joinedQuests)
        }


        fun findQuestByTitle(title: String): Int? {
            return quests.indexOfFirst { it.questName == title }.takeIf { it >= 0 }
        }
        fun findQuestIndexById(id: Int): Int? {
            return quests.indexOfFirst { it.id == id }.takeIf { it != -1 }
        }
        fun generateNewId(): Int {
            return (quests.maxOfOrNull { it.id } ?: 0) + 1
        }
        fun initializeQuests(): ArrayList<Quest> {
            if (quests.isEmpty()) {
                quests.addAll(
                    listOf(
                        Quest(
                            id = 0,
                            questName = "3 x 15 Push-ups",
                            description = "Do 3 sets of 15 push-ups to strengthen your chest and triceps.",
                            questType = "Strength",
                            extraTags = "Chest, Triceps",
                            difficulty = "Normal",
                            xpReward = 50,
                            statReward = 1
                        ),
                        Quest(
                            id = 1,
                            questName = "3 x 10 Pull-ups",
                            description = "Do 3 sets of 10 pull-ups for upper body power.",
                            questType = "Strength",
                            extraTags = "Chest, Triceps",
                            difficulty = "Hard",
                            xpReward = 80,
                            statReward = 1
                        ),
                        Quest(
                            id = 2,
                            questName = "15 min. Meditation",
                            description = "Mindfulness exercise for 15 minutes.",
                            questType = "Vitality",
                            extraTags = "Box Breathe",
                            difficulty = "Normal",
                            xpReward = 60,
                            statReward = 2
                        ),
                        Quest(
                            id = 3,
                            questName = "10km Jog",
                            description = "Jog 10 kilometers to build endurance and stamina.",
                            questType = "Endurance",
                            extraTags = "High Intensity",
                            difficulty = "Extreme",
                            xpReward = 120,
                            statReward = 10
                        ),
                        Quest(
                            id = 4,
                            questName = "3 x 30 Jumping Jacks",
                            description = "Do 3 sets of 30 jumping jacks to warm up and activate full body.",
                            questType = "Endurance",
                            extraTags = "Chest, Triceps",
                            difficulty = "Easy",
                            xpReward = 30,
                            statReward = 5
                        )
                    )
                )
            }

            return ArrayList(quests) // Return a copy of the up-to-date list
        }

    }

}
