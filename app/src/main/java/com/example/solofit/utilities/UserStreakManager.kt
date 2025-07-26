package com.example.solofit.utilities

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.solofit.database.MyDatabaseHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object UserStreakManager {
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStreakIfNeeded(context: Context) {
        val db = MyDatabaseHelper.getInstance(context)!!
        val today = LocalDate.now()
        val todayStr = today.format(DateTimeFormatter.ISO_DATE)
        val completedToday = db.getUserQuestsByStatusDateAndUserID("COMPLETED", todayStr, Extras.DEFAULT_USER_ID)

        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lastStreakDateStr = prefs.getString("last_streak_date", null)
        var streakCount = prefs.getInt("streak_count", 0)
        var longestStreak = prefs.getInt("longest_streak", 0)

        if (completedToday.isNotEmpty()) {
            if (lastStreakDateStr != todayStr) {
                val lastDate = lastStreakDateStr?.let {
                    try {
                        LocalDate.parse(it)
                    } catch (e: Exception) {
                        null
                    }
                }

                val yesterday = today.minusDays(1)

                streakCount = if (lastDate == yesterday) {
                    streakCount + 1
                } else {
                    1
                }

                // Update longest streak if needed
                if (streakCount > longestStreak) {
                    longestStreak = streakCount
                }

                prefs.edit()
                    .putString("last_streak_date", todayStr)
                    .putInt("streak_count", streakCount)
                    .putInt("longest_streak", longestStreak)
                    .apply()
            }
        } else {
            // No completed quests today — check if we missed a day
            val lastDate = lastStreakDateStr?.let {
                try {
                    LocalDate.parse(it)
                } catch (e: Exception) {
                    null
                }
            }

            if (lastDate != null && lastDate.isBefore(today.minusDays(1))) {
                // Missed a day — reset the current streak
                prefs.edit()
                    .putInt("streak_count", 0)
                    .apply()
            }
        }

        Log.d("STREAK_DEBUG", "Today's date: $todayStr")
        Log.d("STREAK_DEBUG", "Completed quests today: ${completedToday.size}")
        completedToday.forEach {
            Log.d("STREAK_DEBUG", "Quest: $it")
        }

    }

    fun getStreakCount(context: Context): Int {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("streak_count", 0)
    }

    fun getLongestStreak(context: Context): Int {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("longest_streak", 0)
    }
}
