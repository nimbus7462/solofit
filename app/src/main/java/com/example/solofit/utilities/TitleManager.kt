    package com.example.solofit.utilities

    import com.example.solofit.model.User

    object TitleManager {

        fun checkAndUnlockTitles(user: User): Boolean {
            val newTitles = getNewlyUnlockedTitles(user)
            if (newTitles.isNotEmpty()) {
                val existingTitles = user.unlockedTitles
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toMutableSet()

                existingTitles.addAll(newTitles)
                user.unlockedTitles = existingTitles.joinToString(",")
                return true  // signal that the user was updated
            }
            return false  // no new titles
        }

        fun getNewlyUnlockedTitles(user: User): List<String> {
            val titles = mutableListOf<String>()
            val strength = user.strengthPts
            val endurance = user.endurancePts
            val vitality = user.vitalityPts
            val streak = user.streakCount
            val level = user.level

            val existing = user.unlockedTitles
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .toSet()

            fun unlock(title: String, condition: Boolean) {
                if (condition && title !in existing) titles.add(title)
            }

            // Combined stat + level
            unlock("Zenith", strength >= 100 && endurance >= 100 && vitality >= 100 && level >= 100)
            unlock("Absolute", strength >= 100 && endurance >= 100 && vitality >= 100)

            // Level-based
            unlock("S Rank Hunter", level >= 100)
            unlock("A Rank Hunter", level >= 75)
            unlock("B Rank Hunter", level >= 50)
            unlock("C Rank Hunter", level >= 25)
            unlock("D Rank Hunter", level >= 10)

            // Streak-based
            unlock("Unbreakable", streak >= 100)
            unlock("Tenacious", streak >= 75)
            unlock("Persistent", streak >= 50)
            unlock("Determined", streak >= 25)

            // Strength
            unlock("Beast of Strength", strength >= 100)
            unlock("Lionheart", strength >= 75)
            unlock("Heavy Hitter", strength >= 50)
            unlock("Power Lifter", strength >= 25)
            unlock("Beginner Brute", strength >= 10)

            // Endurance
            unlock("Iron Will", endurance >= 100)
            unlock("Stamina Lord", endurance >= 75)
            unlock("Long Runner", endurance >= 50)
            unlock("Jogger", endurance >= 25)
            unlock("Breather", endurance >= 10)

            // Vitality
            unlock("Unbreakable Body", vitality >= 100)
            unlock("Resilient Soul", vitality >= 75)
            unlock("Tank", vitality >= 50)
            unlock("Hardy", vitality >= 25)
            unlock("Rookie Defender", vitality >= 10)

            // Default if none yet
            if (existing.isEmpty() && titles.isEmpty()) titles.add("E Rank Hunter")

            return titles
        }
    }
