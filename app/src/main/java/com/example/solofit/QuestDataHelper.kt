import com.example.solofit.model.Quest
import com.example.solofit.R

class QuestDataHelper {

    companion object {
        val quests: MutableList<Quest> = mutableListOf()

        fun addQuest(quest: Quest) {
            quests.add(quest)
        }

        fun updateQuest(index: Int, quest: Quest) {
            quests[index] = quest
        }

        fun findQuestByTitle(title: String): Int? {
            return quests.indexOfFirst { it.title == title }.takeIf { it >= 0 }
        }
        fun findQuestIndexById(id: Int): Int? {
            return quests.indexOfFirst { it.id == id }.takeIf { it != -1 }
        }
        fun generateNewId(): Int {
            return (quests.maxOfOrNull { it.id } ?: 0) + 1
        }fun initializeQuests(): ArrayList<Quest> {
            // Only populate the list once
            if (quests.isEmpty()) {
                quests.addAll(
                    listOf(
                        Quest(
                            id = 0,
                            title = "3 x 15 Push-ups",
                            description = "Do 3 sets of 15 push-ups to strengthen your chest and triceps.",
                            tag = "Strength",
                            addOnTags = "Chest, Triceps",
                            difficulty = "Normal",
                            xpReward = 50,
                            statReward = 1
                        ),
                        Quest(
                            id = 1,
                            title = "3 x 10 Pull-ups",
                            description = "Do 3 sets of 10 pull-ups for upper body power.",
                            tag = "Strength",
                            addOnTags = "Chest, Triceps",
                            difficulty = "Hard",
                            xpReward = 80,
                            statReward = 1

                        ),
                        Quest(
                            id = 2,
                            title = "15 min. Meditation",
                            description = "Mindfulness exercise for 15 minutes.",
                            tag = "Vitality",
                            addOnTags = "Box Breathe",
                            difficulty = "Normal",
                            xpReward = 60,
                            statReward = 2

                        ),
                        Quest(
                            id = 3,
                            title = "10km Jog",
                            description = "Jog 10 kilometers to build endurance and stamina.",
                            tag = "Endurance",
                            addOnTags = "High Intensity",
                            difficulty = "Extreme",
                            xpReward = 120,
                            statReward = 10
                        ),
                        Quest(
                            id = 4,
                            title = "3 x 30 Jumping Jacks",
                            description = "Do 3 sets of 30 jumping jacks to warm up and activate full body.",
                            tag = "Endurance",
                            addOnTags = "Chest, Triceps",
                            difficulty = "Easy",
                            xpReward = 30,
                            statReward = 5
                        )
                    )
                )
            }

            return ArrayList(quests) // return a copy of the up-to-date list
        }
    }

}
