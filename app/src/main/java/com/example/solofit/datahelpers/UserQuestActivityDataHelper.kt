package com.example.solofit.datahelpers

import com.example.solofit.model.UserQuestActivity

class UserQuestActivityDataHelper {

    companion object {
        private val userQuestActs: MutableList<UserQuestActivity> = mutableListOf()

        fun initUQA(): ArrayList<UserQuestActivity> {
            if (userQuestActs.isEmpty()) {
                userQuestActs.addAll(
                    listOf(
                        UserQuestActivity(1, "Completed", "Morning run felt energizing.", "2025-07-15", 0, 1, 1),
                        UserQuestActivity(2, "Aborted", "Had to skip due to illness.", "2025-07-16", 1, 1, 1),
                        UserQuestActivity(3, "Completed", "Finished reading a full chapter.", "2025-07-17", 2, 1, 1),
                        UserQuestActivity(4, "Aborted", "Overwhelmed with work, will retry later.", "2025-07-18", 3, 1, 1),
                        UserQuestActivity(5, "Completed", "Nailed the meditation challenge.", "2025-07-19", 4, 1, 1)
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
