package com.example.solofit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "quest_app.db"
        private const val DATABASE_VERSION = 1

        // Table names
        const val QUEST_TABLE = "quest_table"
        const val QUOTE_TABLE = "quote_table"
        const val QUEST_LOG_TABLE = "quest_log_table"
        const val USER_STATS_TABLE = "user_stats_table"
        const val DAILY_SUMMARY_TABLE = "daily_summary_table"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createQuestTable = """
            CREATE TABLE $QUEST_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                description TEXT,
                tag TEXT,
                add_on_tags TEXT,
                difficulty TEXT,
                xp_reward INTEGER,
                stat_reward INTEGER,
                is_completed INTEGER,
                is_cancelled INTEGER,
                icon INTEGER
            );
        """.trimIndent()

//        val createQuoteTable = """
//            CREATE TABLE $QUOTE_TABLE (
//                quote_id INTEGER PRIMARY KEY AUTOINCREMENT,
//                quote_text TEXT,
//                date_saved TEXT
//            );
//        """.trimIndent()
//
//        val createQuestLogTable = """
//            CREATE TABLE $QUEST_LOG_TABLE (
//                log_id INTEGER PRIMARY KEY AUTOINCREMENT,
//                quest_id INTEGER,
//                log TEXT,
//                timestamp TEXT,
//                FOREIGN KEY (quest_id) REFERENCES $QUEST_TABLE (id)
//            );
//        """.trimIndent()
//
//        val createUserStatsTable = """
//            CREATE TABLE $USER_STATS_TABLE (
//                level INTEGER,
//                xp_total INTEGER,
//                xp_threshold INTEGER,
//                streak INTEGER,
//                strength_points INTEGER,
//                endurance_points INTEGER,
//                vitality_points INTEGER
//                -- Note: completed_quests should be handled via logs or a join table
//            );
//        """.trimIndent()
//
//        val createDailySummaryTable = """
//            CREATE TABLE $DAILY_SUMMARY_TABLE (
//                date TEXT PRIMARY KEY,
//                xp_daily INTEGER,
//                completed_quest_daily INTEGER,
//                quote_of_the_day TEXT,
//                streak_count INTEGER,
//                strength_points_total INTEGER,
//                endurance_points_total INTEGER,
//                vitality_points_total INTEGER
//            );
//        """.trimIndent()

        db.execSQL(createQuestTable)
//        db.execSQL(createQuoteTable)
//        db.execSQL(createQuestLogTable)
//        db.execSQL(createUserStatsTable)
//        db.execSQL(createDailySummaryTable)

        insertInitialQuests(db) // ✅ Seed your initial 5 quests
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $QUEST_TABLE")
//        db.execSQL("DROP TABLE IF EXISTS $QUOTE_TABLE")
//        db.execSQL("DROP TABLE IF EXISTS $QUEST_LOG_TABLE")
//        db.execSQL("DROP TABLE IF EXISTS $USER_STATS_TABLE")
//        db.execSQL("DROP TABLE IF EXISTS $DAILY_SUMMARY_TABLE")
        onCreate(db)
    }

    private fun insertInitialQuests(db: SQLiteDatabase) {
        val quests = listOf(
            Quest(
                id = 0,
                title = "3 x 15 Push-ups",
                description = "Do 3 sets of 15 push-ups to strengthen your chest and triceps.",
                tag = "Strength",
                addOnTags = "Chest, Triceps",
                difficulty = "Medium",
                xpReward = 50,
                statReward = 1,
                isCompleted = false,
                isCancelled = false,
                icon = R.drawable.dumbell_icon
            ),
            Quest(
                id = 0,
                title = "3 x 10 Pull-ups",
                description = "Do 3 sets of 10 pull-ups for upper body power.",
                tag = "Strength",
                addOnTags = "Back, Biceps",
                difficulty = "Hard",
                xpReward = 80,
                statReward = 1,
                isCompleted = false,
                isCancelled = false,
                icon = R.drawable.dumbell_icon
            ),
            Quest(
                id = 0,
                title = "60s Plank",
                description = "Hold a plank for 60 seconds for core strength.",
                tag = "Vitality",
                addOnTags = "Core, Balance",
                difficulty = "Medium",
                xpReward = 60,
                statReward = 2,
                isCompleted = false,
                isCancelled = false,
                icon = R.drawable.meditate
            ),
            Quest(
                id = 0,
                title = "10km Jog",
                description = "Jog 10 kilometers to build endurance and stamina.",
                tag = "Endurance",
                addOnTags = "Cardio, Stamina",
                difficulty = "Hard",
                xpReward = 120,
                statReward = 3,
                isCompleted = false,
                isCancelled = false,
                icon = R.drawable.footprint
            ),
            Quest(
                id = 0,
                title = "3 x 30 Jumping Jacks",
                description = "Do 3 sets of 30 jumping jacks to warm up and activate full body.",
                tag = "Endurance",
                addOnTags = "Warm-up, Cardio",
                difficulty = "Easy",
                xpReward = 30,
                statReward = 1,
                isCompleted = false,
                isCancelled = false,
                icon = R.drawable.footprint
            )
        )

        for (quest in quests) {
            val values = ContentValues().apply {
                put("title", quest.title)
                put("description", quest.description)
                put("tag", quest.tag)
                put("add_on_tags", quest.addOnTags)
                put("difficulty", quest.difficulty)
                put("xp_reward", quest.xpReward)
                put("stat_reward", quest.statReward)
                put("is_completed", if (quest.isCompleted) 1 else 0)
                put("is_cancelled", if (quest.isCancelled) 1 else 0)
                put("icon", quest.icon)
            }
            db.insert(QUEST_TABLE, null, values)
        }
    }

    fun addQuest(quest: Quest): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", quest.title)
            put("description", quest.description)
            put("tag", quest.tag)
            put("add_on_tags", quest.addOnTags)
            put("difficulty", quest.difficulty)
            put("xp_reward", quest.xpReward)
            put("stat_reward", quest.statReward)
            put("is_completed", if (quest.isCompleted) 1 else 0)
            put("is_cancelled", if (quest.isCancelled) 1 else 0)
            put("icon", quest.icon)
        }
        val result = db.insert(QUEST_TABLE, null, values)
        db.close()
        return result != -1L
    }

    fun updateQuest(quest: Quest): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", quest.title)
            put("description", quest.description)
            put("tag", quest.tag)
            put("add_on_tags", quest.addOnTags)
            put("difficulty", quest.difficulty)
            put("xp_reward", quest.xpReward)
            put("stat_reward", quest.statReward)
            put("is_completed", if (quest.isCompleted) 1 else 0)
            put("is_cancelled", if (quest.isCancelled) 1 else 0)
            put("icon", quest.icon)
        }
        val result = db.update(QUEST_TABLE, values, "id=?", arrayOf(quest.id.toString()))
        db.close()
        return result > 0
    }

    fun deleteQuest(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(QUEST_TABLE, "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun getAllQuests(): List<Quest> {
        val quests = mutableListOf<Quest>()
        val db = readableDatabase
        val cursor = db.query(QUEST_TABLE, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                quests.add(
                    Quest(
                        id = getInt(getColumnIndexOrThrow("id")),
                        title = getString(getColumnIndexOrThrow("title")),
                        description = getString(getColumnIndexOrThrow("description")),
                        tag = getString(getColumnIndexOrThrow("tag")),
                        addOnTags = getString(getColumnIndexOrThrow("add_on_tags")),
                        difficulty = getString(getColumnIndexOrThrow("difficulty")),
                        xpReward = getInt(getColumnIndexOrThrow("xp_reward")),
                        statReward = getInt(getColumnIndexOrThrow("stat_reward")),
                        isCompleted = getInt(getColumnIndexOrThrow("is_completed")) == 1,
                        isCancelled = getInt(getColumnIndexOrThrow("is_cancelled")) == 1,
                        icon = getInt(getColumnIndexOrThrow("icon"))
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return quests
    }
}
