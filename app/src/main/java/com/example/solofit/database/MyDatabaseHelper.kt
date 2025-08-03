package com.example.solofit.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.solofit.model.Quest
import com.example.solofit.model.Quote
import com.example.solofit.model.User
import com.example.solofit.model.UserQuestActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION
) {

    // Singleton instance
    companion object {
        private var instance: MyDatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDatabaseHelper? {
            if (instance == null) {
                Log.d("Table created", "Table created successfully from getInstance")
                instance = MyDatabaseHelper(context.applicationContext)
            }
            return instance
        }
    }

    // Step 0: All constants for structure, columns, and SQL
    private object DbReferences {
        const val DATABASE_NAME = "quest_app.db"
        const val DATABASE_VERSION = 10 // Added Streaks to user

        // Quest Table
        const val TABLE_QUEST = "quest_table"
        const val COLUMN_QUEST_ID = "quest_id"
        const val COLUMN_QUEST_NAME = "quest_name"            // updated
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_QUEST_TYPE = "quest_type"            // updated
        const val COLUMN_EXTRA_TAGS = "extra_tags"            // updated
        const val COLUMN_DIFFICULTY = "difficulty"
        const val COLUMN_XP_REWARD = "xp_reward"
        const val COLUMN_STAT_REWARD = "stat_reward"

        // Quote Table (unchanged)
        const val TABLE_QUOTE = "quote_table"
        const val COLUMN_QUOTE_ID = "quote_id"
        const val COLUMN_QUOTE_TEXT = "quote_text"
        const val COLUMN_QUOTE_AUTHOR = "quote_author"
        const val COLUMN_IS_SAVED = "is_saved"

        // User Table
        const val TABLE_USER = "user_table"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PFP_URI = "pfp_uri"
        const val COLUMN_SELECTED_TITLE = "selected_title"
        const val COLUMN_LEVEL = "level"
        const val COLUMN_EXP = "current_exp"
        const val COLUMN_EXP_CAP = "exp_cap"
        const val COLUMN_STR = "strength_pts"
        const val COLUMN_END = "endurance_pts"
        const val COLUMN_VIT = "vitality_pts"
        const val COLUMN_STREAK_COUNT = "streak_count"
        const val COLUMN_LONGEST_STREAK = "longest_streak"
        const val COLUMN_LAST_STREAK_DATE = "last_streak_date"
        const val COLUMN_UNLOCKED_TITLES = "unlocked_titles"


        // UserQuestActivity Table
        const val TABLE_UQA = "user_quest_activity"
        const val COLUMN_UQA_ID = "user_quest_act_id"
        const val COLUMN_QUEST_STATUS = "quest_status"
        const val COLUMN_USER_LOGS = "user_logs"
        const val COLUMN_DATE_CREATED = "date_created"       // updated
        const val COLUMN_UQA_QUEST_ID = "quest_id"
        const val COLUMN_UQA_QUOTE_ID = "quote_id"
        const val COLUMN_UQA_USER_ID = "user_id"

        // --- SQL Create Table Statements ---
        const val CREATE_TABLE_QUEST = """
        CREATE TABLE $TABLE_QUEST (
            $COLUMN_QUEST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_QUEST_NAME TEXT,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_QUEST_TYPE TEXT,
            $COLUMN_EXTRA_TAGS TEXT,
            $COLUMN_DIFFICULTY TEXT,
            $COLUMN_XP_REWARD INTEGER,
            $COLUMN_STAT_REWARD INTEGER     
        );
    """
            const val CREATE_TABLE_USER = """
        CREATE TABLE $TABLE_USER (
            $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_USERNAME TEXT,
            $COLUMN_PFP_URI TEXT,
            $COLUMN_SELECTED_TITLE TEXT,       -- renamed
            $COLUMN_LEVEL INTEGER,
            $COLUMN_EXP REAL,
            $COLUMN_EXP_CAP INTEGER,
            $COLUMN_STR INTEGER,
            $COLUMN_END INTEGER,
            $COLUMN_VIT INTEGER,
            $COLUMN_LAST_STREAK_DATE TEXT,
            $COLUMN_STREAK_COUNT INTEGER,
            $COLUMN_LONGEST_STREAK INTEGER,
            $COLUMN_UNLOCKED_TITLES TEXT
        );
    """


        const val CREATE_TABLE_UQA = """
        CREATE TABLE $TABLE_UQA (
            $COLUMN_UQA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_QUEST_STATUS TEXT,
            $COLUMN_USER_LOGS TEXT,
            $COLUMN_DATE_CREATED TEXT,
            $COLUMN_UQA_QUEST_ID INTEGER,
            $COLUMN_UQA_QUOTE_ID INTEGER,
            $COLUMN_UQA_USER_ID INTEGER,
            FOREIGN KEY ($COLUMN_UQA_QUEST_ID) REFERENCES $TABLE_QUEST($COLUMN_QUEST_ID),
            FOREIGN KEY ($COLUMN_UQA_QUOTE_ID) REFERENCES $TABLE_QUOTE($COLUMN_QUOTE_ID),
            FOREIGN KEY ($COLUMN_UQA_USER_ID) REFERENCES $TABLE_USER($COLUMN_USER_ID)
        );
    """

        const val CREATE_TABLE_QUOTE = """
        CREATE TABLE $TABLE_QUOTE (
            $COLUMN_QUOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_QUOTE_TEXT TEXT,
            $COLUMN_QUOTE_AUTHOR TEXT,
            $COLUMN_IS_SAVED INTEGER
        );
    """

        // Drop statements (unchanged)
        const val DROP_TABLE_QUEST = "DROP TABLE IF EXISTS $TABLE_QUEST"
        const val DROP_TABLE_QUOTE = "DROP TABLE IF EXISTS $TABLE_QUOTE"
        const val DROP_TABLE_USER = "DROP TABLE IF EXISTS $TABLE_USER"
        const val DROP_TABLE_UQA = "DROP TABLE IF EXISTS $TABLE_UQA"
    }



    /* Step 3: Create the table when DB is first initialized */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbReferences.CREATE_TABLE_QUEST)
        db.execSQL(DbReferences.CREATE_TABLE_QUOTE)
        db.execSQL(DbReferences.CREATE_TABLE_USER)
        db.execSQL(DbReferences.CREATE_TABLE_UQA)
        insertInitialQuests(db)
        insertInitialUser(db)
    }


    /* Step 4: Drop and recreate the table when DB version changes */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DbReferences.DROP_TABLE_UQA)
        db.execSQL(DbReferences.DROP_TABLE_USER)
        db.execSQL(DbReferences.DROP_TABLE_QUOTE)
        db.execSQL(DbReferences.DROP_TABLE_QUEST)
        onCreate(db)
    }

    // Helper method
    /* Step 5a: Insert seed data into the quest table on first install */
    private fun insertInitialQuests(db: SQLiteDatabase) {
        val quests = listOf(
            Quest("3 x 15 Push-ups", "Do 3 sets of 15 push-ups to strengthen your chest and triceps.", "Strength", "Chest, Triceps", "Normal", 50, 1),
            Quest("3 x 10 Pull-ups", "Do 3 sets of 10 pull-ups for upper body power.", "Strength", "Back, Biceps", "Hard", 80, 1),
            Quest("60s Plank", "Hold a plank for 60 seconds for core strength.", "Vitality", "Core, Balance", "Normal", 60, 2),
            Quest("10km Jog", "Jog 10 kilometers to build endurance and stamina.", "Endurance", "Cardio, Stamina", "Hard", 120, 3),
            Quest("3 x 30 Jumping Jacks", "Do 3 sets of 30 jumping jacks to warm up and activate full body.", "Endurance", "Warm-up, Cardio", "Easy", 30, 1),
            Quest("5 Min Wall Sit", "Hold a wall sit position for 5 minutes to improve lower body endurance.", "Vitality", "Legs, Core", "Hard", 90, 2),
            Quest("3 x 20 Bodyweight Squats", "Perform 3 sets of 20 bodyweight squats for leg strength.", "Strength", "Legs, Glutes", "Normal", 60, 1),
            Quest("5 x 1 Min Jump Rope", "Jump rope for 1 minute, 5 rounds, to improve cardio and coordination.", "Endurance", "Cardio, Footwork", "Normal", 45, 2),
            Quest("3 x 20 Mountain Climbers", "Do 3 sets of 20 mountain climbers to work your core and agility.", "Vitality", "Core, Full Body", "Normal", 55, 1),
            Quest("1 Min High Knees", "Do high knees for 1 minute to elevate heart rate and warm up.", "Endurance", "Legs, Cardio", "Easy", 20, 1),
            Quest("3 x 15 Dips", "Use parallel bars or a bench to perform dips for triceps and chest.", "Strength", "Triceps, Chest", "Normal", 60, 1),
            Quest("10 Min Yoga Flow", "Perform a 10-minute yoga session to improve flexibility and balance.", "Vitality", "Flexibility, Balance", "Easy", 40, 1),
            Quest("3 x 12 Lunges", "Alternate legs for 3 sets of 12 lunges to strengthen lower body.", "Strength", "Legs, Glutes", "Normal", 55, 1),
            Quest("3 x 10 Burpees", "Do 3 sets of 10 burpees for full body explosive movement.", "Endurance", "Full Body, Cardio", "Hard", 90, 2),
            Quest("15 Min Walk", "Take a 15-minute brisk walk to stay active and refreshed.", "Endurance", "Cardio, Light Activity", "Easy", 15, 1)
        )


        for (q in quests) {
            val values = ContentValues().apply {
                put(DbReferences.COLUMN_QUEST_NAME, q.questName)
                put(DbReferences.COLUMN_DESCRIPTION, q.description)
                put(DbReferences.COLUMN_QUEST_TYPE, q.questType)
                put(DbReferences.COLUMN_EXTRA_TAGS, q.extraTags)
                put(DbReferences.COLUMN_DIFFICULTY, q.difficulty)
                put(DbReferences.COLUMN_XP_REWARD, q.xpReward)
                put(DbReferences.COLUMN_STAT_REWARD, q.statReward)
            }
            db.insert(DbReferences.TABLE_QUEST, null, values)
        }
    }

    private fun insertInitialUser(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_USERNAME, "Player")
            put(DbReferences.COLUMN_PFP_URI, null as String?) // default no profile picture
            put(DbReferences.COLUMN_SELECTED_TITLE, "")
            put(DbReferences.COLUMN_LEVEL, 1)
            put(DbReferences.COLUMN_EXP, 0.0f)
            put(DbReferences.COLUMN_EXP_CAP, 10)
            put(DbReferences.COLUMN_STR, 0)
            put(DbReferences.COLUMN_END, 0)
            put(DbReferences.COLUMN_VIT, 0)
            put(DbReferences.COLUMN_STREAK_COUNT, 0)
            put(DbReferences.COLUMN_LONGEST_STREAK, 0)
            put(DbReferences.COLUMN_LAST_STREAK_DATE, null as String?)
            put(DbReferences.COLUMN_UNLOCKED_TITLES, "")
        }

        db.insert(DbReferences.TABLE_USER, null, values)
    }


    fun getAllQuests(): ArrayList<Quest> {
        val database = this.readableDatabase
        val c = database.query(
            DbReferences.TABLE_QUEST,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val quests: ArrayList<Quest> = ArrayList()

        while (c.moveToNext()) {
            quests.add(
                Quest(
                    id = c.getInt(c.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_ID)),
                    questName = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_NAME)),
                    description = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_DESCRIPTION)),
                    questType = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_TYPE)),
                    extraTags = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_EXTRA_TAGS)),
                    difficulty = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_DIFFICULTY)),
                    xpReward = c.getInt(c.getColumnIndexOrThrow(DbReferences.COLUMN_XP_REWARD)),
                    statReward = c.getInt(c.getColumnIndexOrThrow(DbReferences.COLUMN_STAT_REWARD))
                )
            )
        }

        c.close()
        database.close()

        return quests
    }

    @Synchronized
    fun insertQuest(q: Quest): Long {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUEST_NAME, q.questName)
            put(DbReferences.COLUMN_DESCRIPTION, q.description)
            put(DbReferences.COLUMN_QUEST_TYPE, q.questType)
            put(DbReferences.COLUMN_EXTRA_TAGS, q.extraTags)
            put(DbReferences.COLUMN_DIFFICULTY, q.difficulty)
            put(DbReferences.COLUMN_XP_REWARD, q.xpReward)
            put(DbReferences.COLUMN_STAT_REWARD, q.statReward)
        }

        val id = database.insert(DbReferences.TABLE_QUEST, null, values)
        database.close()
        return id
    }

    fun updateQuest(q: Quest): Int {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUEST_NAME, q.questName)
            put(DbReferences.COLUMN_DESCRIPTION, q.description)
            put(DbReferences.COLUMN_QUEST_TYPE, q.questType)
            put(DbReferences.COLUMN_EXTRA_TAGS, q.extraTags)
            put(DbReferences.COLUMN_DIFFICULTY, q.difficulty)
            put(DbReferences.COLUMN_XP_REWARD, q.xpReward)
            put(DbReferences.COLUMN_STAT_REWARD, q.statReward)
        }

        val selection = DbReferences.COLUMN_QUEST_ID + " = ?"
        val selectionArgs = arrayOf(q.id.toString())

        val rowsUpdated = database.update(DbReferences.TABLE_QUEST, values, selection, selectionArgs)
        database.close()

        return rowsUpdated
    }


    fun deleteQuest(id: Int) {
        val database = this.writableDatabase

        // WHERE clause
        val selection = DbReferences.COLUMN_QUEST_ID + " = ?"
        val selectionArgs = arrayOf(id.toString())

        // Executes the delete
        database.delete(DbReferences.TABLE_QUEST, selection, selectionArgs)

        database.close()
    }

    // helper function to get Quest ID
    fun getQuestById(id: Int): Quest? {
        val database = this.readableDatabase

        val cursor = database.query(
            DbReferences.TABLE_QUEST,
            null,
            "${DbReferences.COLUMN_QUEST_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var quest: Quest? = null
        if (cursor.moveToFirst()) {
            quest = Quest(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_ID)),
                questName = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DESCRIPTION)),
                questType = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_TYPE)),
                extraTags = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_EXTRA_TAGS)),
                difficulty = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DIFFICULTY)),
                xpReward = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_XP_REWARD)),
                statReward = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_STAT_REWARD))
            )
        }

        cursor.close()
        database.close()
        return quest
    }

    // Step 1: Get 5 random quests from the quest table
    fun getRandomQuests(limit: Int = 5): List<Quest> {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM ${DbReferences.TABLE_QUEST} ORDER BY RANDOM() LIMIT $limit",
            null
        )
        val quests = mutableListOf<Quest>()
        while (cursor.moveToNext()) {
            quests.add(
                Quest(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_ID)),
                    questName = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DESCRIPTION)),
                    questType = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_TYPE)),
                    extraTags = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_EXTRA_TAGS)),
                    difficulty = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DIFFICULTY)),
                    xpReward = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_XP_REWARD)),
                    statReward = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_STAT_REWARD))
                )
            )
        }
        cursor.close()
        database.close()
        return quests
    }

    // Step 2: Get UserQuestActivities by status, date, and userID
    fun getUserQuestsByStatusDateAndUserID(
        status: String?,
        date: String,
        userID: Int? = null
    ): List<UserQuestActivity> {
        val database = this.readableDatabase

        val conditions = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (status != null) {
            conditions.add("${DbReferences.COLUMN_QUEST_STATUS} = ?")
            args.add(status)
        }

        conditions.add("${DbReferences.COLUMN_DATE_CREATED} = ?")
        args.add(date)

        if (userID != null) {
            conditions.add("${DbReferences.COLUMN_UQA_USER_ID} = ?")
            args.add(userID.toString())
        }

        val selection = conditions.joinToString(" AND ")
        val selectionArgs = args.toTypedArray()

        val cursor = database.query(
            DbReferences.TABLE_UQA,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val result = mutableListOf<UserQuestActivity>()
        while (cursor.moveToNext()) {
            result.add(
                UserQuestActivity(
                    userQuestActID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_ID)),
                    questStatus = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_STATUS)),
                    userLogs = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_USER_LOGS)),
                    dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DATE_CREATED)),
                    questID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_QUEST_ID)),
                    quoteID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_QUOTE_ID)),
                    userID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_USER_ID))
                )
            )
        }

        cursor.close()
        database.close()
        return result
    }


    // Step 3: Automatically cancel unfinished quests from past days
    fun autoCancelOldUnfinishedQuests(today: String) {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUEST_STATUS, "ABORTED")
        }

        val rowsUpdated = database.update(
            DbReferences.TABLE_UQA,
            values,
            "${DbReferences.COLUMN_QUEST_STATUS} = ? AND ${DbReferences.COLUMN_DATE_CREATED} < ?",
            arrayOf("CREATED", today)
        )

        Log.d("AutoCancel", "Auto-cancelled $rowsUpdated old quests")
        database.close()
    }

    fun getUserQuestActivityById(uqaId: Int): UserQuestActivity? {
        val database = this.readableDatabase

        val cursor = database.query(
            DbReferences.TABLE_UQA,
            null,
            "${DbReferences.COLUMN_UQA_ID} = ?",
            arrayOf(uqaId.toString()),
            null,
            null,
            null
        )

        val result = if (cursor.moveToFirst()) {
            UserQuestActivity(
                userQuestActID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_ID)),
                questStatus = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_STATUS)),
                userLogs = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_USER_LOGS)),
                dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DATE_CREATED)),
                questID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_QUEST_ID)),
                quoteID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_QUOTE_ID)),
                userID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_USER_ID))
            )
        } else {
            null
        }

        cursor.close()
        database.close()
        return result
    }



    /* QUOTE TABLE CRUD */
    fun insertQuote(quote: Quote): Long {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUOTE_TEXT, quote.quoteText)
            put(DbReferences.COLUMN_QUOTE_AUTHOR, quote.quoteAuthor)
            put(DbReferences.COLUMN_IS_SAVED, 0) // false by default
            // put(DbReferences.COLUMN_IS_SAVED, if (quote.isSaved) 1 else 0) // convert Boolean to Int
        }
        val id = database.insert(DbReferences.TABLE_QUOTE, null, values)
        database.close()
        return id
    }

    fun getAllQuotes(): ArrayList<Quote> {
        val quotes = ArrayList<Quote>()
        val database = this.readableDatabase
        val cursor = database.query(DbReferences.TABLE_QUOTE, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            quotes.add(
                Quote(
                    quoteID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_ID)),
                    quoteText = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_TEXT)),
                    quoteAuthor = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_AUTHOR)),
                    isSaved = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_IS_SAVED)) == 1
                )
            )
        }
        cursor.close()
        database.close()
        return quotes
    }

    fun updateQuote(quote: Quote) {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUOTE_TEXT, quote.quoteText)
            put(DbReferences.COLUMN_QUOTE_AUTHOR, quote.quoteAuthor)
            put(DbReferences.COLUMN_IS_SAVED, if (quote.isSaved) 1 else 0)
        }
        val selection = "${DbReferences.COLUMN_QUOTE_ID} = ?"
        val selectionArgs = arrayOf(quote.quoteID.toString())
        database.update(DbReferences.TABLE_QUOTE, values, selection, selectionArgs)
        database.close()
    }

    fun deleteQuote(id: Int) {
        val database = this.writableDatabase
        val selection = "${DbReferences.COLUMN_QUOTE_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        database.delete(DbReferences.TABLE_QUOTE, selection, selectionArgs)
        database.close()
    }

    // Helper function
    fun getRandomQuote(): Quote? {
        val db = this.readableDatabase

        // Only select quotes that are NOT saved/bookmarked (isSaved = 0)
        // - Maybe add a checker where is saved quotes wont appear?
        // WHERE ${DbReferences.COLUMN_IS_SAVED} = 0 to ensure only unsaved quotes are shown.
        val cursor = db.rawQuery(
            "SELECT * FROM ${DbReferences.TABLE_QUOTE} WHERE ${DbReferences.COLUMN_IS_SAVED} = 0 ORDER BY RANDOM() LIMIT 1",
            null
        )

        var quote: Quote? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_ID))
            val text = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_TEXT))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_AUTHOR))
            val isSaved = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_IS_SAVED)) == 1
            quote = Quote(id, text, author, isSaved)
        }

        cursor.close()
        db.close()
        return quote
    }

    fun updateQuoteSaveStatus(quoteID: Int, isSaved: Boolean): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_IS_SAVED, if (isSaved) 1 else 0)
        }
        return db.update(
            DbReferences.TABLE_QUOTE,
            values,
            "${DbReferences.COLUMN_QUOTE_ID} = ?",
            arrayOf(quoteID.toString())
        )
    }



    /* USER TABLE CRUD */
    fun insertUser(user: User): Long {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_USERNAME, user.username)
            put(DbReferences.COLUMN_PFP_URI, user.pfpUri)
            put(DbReferences.COLUMN_SELECTED_TITLE, user.selectedTitle)
            put(DbReferences.COLUMN_LEVEL, user.level)
            put(DbReferences.COLUMN_EXP, user.currentExp)
            put(DbReferences.COLUMN_EXP_CAP, user.expCap)
            put(DbReferences.COLUMN_STR, user.strengthPts)
            put(DbReferences.COLUMN_END, user.endurancePts)
            put(DbReferences.COLUMN_VIT, user.vitalityPts)
            put(DbReferences.COLUMN_STREAK_COUNT, user.streakCount)
            put(DbReferences.COLUMN_LONGEST_STREAK, user.longestStreak)
            put(DbReferences.COLUMN_LAST_STREAK_DATE, user.lastStreakDate)
            put(DbReferences.COLUMN_UNLOCKED_TITLES, user.unlockedTitles)
        }
        val id = database.insert(DbReferences.TABLE_USER, null, values)
        database.close()
        return id
    }




    fun logAllUsers() {
        val database = this.readableDatabase
        val cursor = database.rawQuery("SELECT * FROM ${DbReferences.TABLE_USER}", null)
        if (cursor.moveToFirst()) {
            do {
                val userID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_USER_ID))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_USERNAME))
                val pfpUri = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_PFP_URI))
                val selectedTitle = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_SELECTED_TITLE))
                val level = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LEVEL))
                val exp = cursor.getFloat(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_EXP))
                val cap = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_EXP_CAP))
                val str = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_STR))
                val end = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_END))
                val vit = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_VIT))
                val streak = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_STREAK_COUNT))
                val longest = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LONGEST_STREAK))
                val lastDate = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LAST_STREAK_DATE))
                val unlockedTitles = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UNLOCKED_TITLES))

                android.util.Log.d(
                    "USER_LOG",
                    "User($userID, $username, $pfpUri, selectedTitle=$selectedTitle, unlocked=[$unlockedTitles], level=$level, exp=$exp/$cap, STR=$str, END=$end, VIT=$vit, streak=$streak, longest=$longest, last=$lastDate)"
                )
            } while (cursor.moveToNext())
        } else {
            android.util.Log.d("USER_LOG", "No users found in the table.")
        }
        cursor.close()
        database.close()
    }

    fun getUserById(id: Int): User? {
        val database = this.readableDatabase
        val cursor = database.query(
            DbReferences.TABLE_USER,
            null,
            "${DbReferences.COLUMN_USER_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                userID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_USERNAME)),
                pfpUri = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_PFP_URI)),
                selectedTitle = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_SELECTED_TITLE)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LEVEL)),
                currentExp = cursor.getFloat(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_EXP)),
                expCap = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_EXP_CAP)),
                strengthPts = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_STR)),
                endurancePts = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_END)),
                vitalityPts = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_VIT)),
                streakCount = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_STREAK_COUNT)),
                longestStreak = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LONGEST_STREAK)),
                lastStreakDate = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LAST_STREAK_DATE)),
                unlockedTitles = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UNLOCKED_TITLES)) ?: "",
                )
        }
        cursor.close()
        database.close()
        return user
    }


    fun updateUserStreakIfNeeded(user: User): User {
        val today = LocalDate.now()
        val todayStr = today.format(DateTimeFormatter.ISO_DATE)

        val lastDate = user.lastStreakDate?.let {
            try {
                LocalDate.parse(it)
            } catch (e: Exception) {
                null
            }
        }
        val yesterday = today.minusDays(1)

        if (lastDate != today) {
            user.lastStreakDate = todayStr
            user.streakCount = if (lastDate == yesterday) user.streakCount + 1 else 1
            user.longestStreak = maxOf(user.longestStreak, user.streakCount)
            updateUser(user) // Persist changes to DB
        }

        return user
    }




    fun updateUser(user: User) {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_USERNAME, user.username)
            put(DbReferences.COLUMN_PFP_URI, user.pfpUri)
            put(DbReferences.COLUMN_SELECTED_TITLE, user.selectedTitle)
            put(DbReferences.COLUMN_LEVEL, user.level)
            put(DbReferences.COLUMN_EXP, user.currentExp)
            put(DbReferences.COLUMN_EXP_CAP, user.expCap)
            put(DbReferences.COLUMN_STR, user.strengthPts)
            put(DbReferences.COLUMN_END, user.endurancePts)
            put(DbReferences.COLUMN_VIT, user.vitalityPts)
            put(DbReferences.COLUMN_STREAK_COUNT, user.streakCount)
            put(DbReferences.COLUMN_LONGEST_STREAK, user.longestStreak)
            put(DbReferences.COLUMN_LAST_STREAK_DATE, user.lastStreakDate)
            put(DbReferences.COLUMN_UNLOCKED_TITLES, user.unlockedTitles)
        }
        val selection = "${DbReferences.COLUMN_USER_ID} = ?"
        val selectionArgs = arrayOf(user.userID.toString())
        database.update(DbReferences.TABLE_USER, values, selection, selectionArgs)
        database.close()
    }



    fun deleteUser(id: Int) {
        val database = this.writableDatabase
        val selection = "${DbReferences.COLUMN_USER_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        database.delete(DbReferences.TABLE_USER, selection, selectionArgs)
        database.close()
    }

    /* USER QUEST ACTIVITY TABLE CRUD */
    fun insertUserQuestActivity(uqa: UserQuestActivity): Long {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUEST_STATUS, uqa.questStatus)
            put(DbReferences.COLUMN_USER_LOGS, uqa.userLogs)
            put(DbReferences.COLUMN_DATE_CREATED, uqa.dateCreated)  // updated
            put(DbReferences.COLUMN_UQA_QUEST_ID, uqa.questID)
            put(DbReferences.COLUMN_UQA_QUOTE_ID, uqa.quoteID)
            put(DbReferences.COLUMN_UQA_USER_ID, uqa.userID)
        }
        val id = database.insert(DbReferences.TABLE_UQA, null, values)
        database.close()
        return id
    }


    fun getAllUserQuestActivities(): ArrayList<UserQuestActivity> {
        val activities = ArrayList<UserQuestActivity>()
        val database = this.readableDatabase
        val cursor = database.query(DbReferences.TABLE_UQA, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            activities.add(
                UserQuestActivity(
                    userQuestActID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_ID)),
                    questStatus = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUEST_STATUS)),
                    userLogs = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_USER_LOGS)),
                    dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DATE_CREATED)),  // updated
                    questID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_QUEST_ID)),
                    quoteID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_QUOTE_ID)),
                    userID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_UQA_USER_ID))
                )
            )
        }
        cursor.close()
        database.close()
        return activities
    }


    fun updateUserQuestActivity(uqa: UserQuestActivity) {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUEST_STATUS, uqa.questStatus)
            put(DbReferences.COLUMN_USER_LOGS, uqa.userLogs)
            put(DbReferences.COLUMN_DATE_CREATED, uqa.dateCreated)  // updated
            put(DbReferences.COLUMN_UQA_QUEST_ID, uqa.questID)
            put(DbReferences.COLUMN_UQA_QUOTE_ID, uqa.quoteID)
            put(DbReferences.COLUMN_UQA_USER_ID, uqa.userID)
        }
        val selection = "${DbReferences.COLUMN_UQA_ID} = ?"
        val selectionArgs = arrayOf(uqa.userQuestActID.toString())
        database.update(DbReferences.TABLE_UQA, values, selection, selectionArgs)
        database.close()
    }


    fun deleteUserQuestActivity(id: Int) {
        val database = this.writableDatabase
        val selection = "${DbReferences.COLUMN_UQA_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        database.delete(DbReferences.TABLE_UQA, selection, selectionArgs)
        database.close()
    }
    fun getAllSavedQuotes(): ArrayList<Quote> {
        val quotes = ArrayList<Quote>()
        val database = this.readableDatabase

        val cursor = database.query(
            DbReferences.TABLE_QUOTE,
            null,
            "${DbReferences.COLUMN_IS_SAVED} = ?",
            arrayOf("1"),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            quotes.add(
                Quote(
                    quoteID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_ID)),
                    quoteText = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_TEXT)),
                    quoteAuthor = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_AUTHOR)),
                    isSaved = true
                )
            )
        }

        cursor.close()
        database.close()
        return quotes
    }
    fun getQuoteById(id: Int): Quote? {
        val database = this.readableDatabase
        val cursor = database.query(
            DbReferences.TABLE_QUOTE,
            null,
            "${DbReferences.COLUMN_QUOTE_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        var quote: Quote? = null
        if (cursor.moveToFirst()) {
            quote = Quote(
                quoteID = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_ID)),
                quoteText = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_TEXT)),
                quoteAuthor = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_QUOTE_AUTHOR)),
                isSaved = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_IS_SAVED)) == 1
            )
        }
        cursor.close()
        database.close()
        return quote
    }
}

