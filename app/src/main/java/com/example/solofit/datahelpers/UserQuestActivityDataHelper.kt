package com.example.solofit.datahelpers

import com.example.solofit.model.UserQuestActivity

class UserQuestActivityDataHelper {

    companion object {
        private val userQuestActs: MutableList<UserQuestActivity> = mutableListOf()

        fun initUQA(): ArrayList<UserQuestActivity> {
            if (userQuestActs.isEmpty()) {
                userQuestActs.addAll(
                    listOf(
                        UserQuestActivity(
                            userQuestActID = 1,
                            questStatus = "Completed",
                            userLogs = "Morning run felt energizing.",
                            dateCompleted = "2025-07-15",
                            questID = 0,
                            quoteID = 1,
                            userID = 1
                        ),
                        UserQuestActivity(
                            userQuestActID = 2,
                            questStatus = "Aborted",
                            userLogs = "Had to skip due to illness.",
                            dateCompleted = "2025-07-16",
                            questID = 1,
                            quoteID = 1,
                            userID = 1
                        ),
                        UserQuestActivity(
                            userQuestActID = 3,
                            questStatus = "Completed",
                            userLogs = "Finished reading a full chapter.",
                            dateCompleted = "2025-07-17",
                            questID = 2,
                            quoteID = 1,
                            userID = 1
                        ),
                        UserQuestActivity(
                            userQuestActID = 4,
                            questStatus = "Aborted",
                            userLogs = "Overwhelmed with work, will retry later.",
                            dateCompleted = "2025-07-18",
                            questID = 3,
                            quoteID = 1,
                            userID = 1
                        ),
                        UserQuestActivity(
                            userQuestActID = 5,
                            questStatus = "Completed",
                            userLogs = "Nailed the meditation challenge.",
                            dateCompleted = "2025-07-19",
                            questID = 4,
                            quoteID = 1,
                            userID = 1
                        )
                    )
                )
            }

            return ArrayList(userQuestActs)
        }

        fun getByUserId(userId: Int): List<UserQuestActivity> {
            return userQuestActs.filter { it.userID == userId }
        }

        fun addUQA(uqa: UserQuestActivity) {
            userQuestActs.add(uqa)
        }
        fun updateUserLog(userQuestActID: Int, newLog: String): Boolean {
            val uqa = userQuestActs.find { it.userQuestActID == userQuestActID }
            return if (uqa != null) {
                uqa.userLogs = newLog
                true
            } else {
                false
            }
        }


        fun clear() {
            userQuestActs.clear()
        }
    }
}
