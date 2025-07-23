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

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION
) {

    // Singleton instance
    // The singleton pattern design, step 2
    companion object {
        private var instance: MyDatabaseHelper? = null

        // only opens one db connection
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
        const val DATABASE_VERSION = 2      // change if u wanna change smth in the table

        // Quest Table
        const val TABLE_QUEST = "quest_table"
        const val COLUMN_QUEST_ID = "quest_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TAG = "tag"
        const val COLUMN_ADD_ON_TAGS = "add_on_tags"
        const val COLUMN_DIFFICULTY = "difficulty"
        const val COLUMN_XP_REWARD = "xp_reward"
        const val COLUMN_STAT_REWARD = "stat_reward"
        const val COLUMN_ICON = "icon"

        // Quote Table
        const val TABLE_QUOTE = "quote_table"
        const val COLUMN_QUOTE_ID = "quote_id"
        const val COLUMN_QUOTE_TEXT = "quote_text"
        const val COLUMN_QUOTE_AUTHOR = "quote_author"
        const val COLUMN_IS_SAVED = "is_saved"

        // User Table
        const val TABLE_USER = "user_table"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_LEVEL = "level"
        const val COLUMN_EXP = "current_exp"
        const val COLUMN_LEVEL_CAP = "level_cap"
        const val COLUMN_STR = "strength_pts"
        const val COLUMN_END = "endurance_pts"
        const val COLUMN_VIT = "vitality_pts"

        // UserQuestActivity Table
        const val TABLE_UQA = "user_quest_activity"
        const val COLUMN_UQA_ID = "user_quest_act_id"
        const val COLUMN_QUEST_STATUS = "quest_status"
        const val COLUMN_USER_LOGS = "user_logs"
        const val COLUMN_DATE_COMPLETED = "date_completed"
        const val COLUMN_UQA_QUEST_ID = "quest_id"
        const val COLUMN_UQA_QUOTE_ID = "quote_id"
        const val COLUMN_UQA_USER_ID = "user_id"

        // SQL: Quest Table
        const val CREATE_TABLE_QUEST = """
        CREATE TABLE $TABLE_QUEST (
            $COLUMN_QUEST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITLE TEXT,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_TAG TEXT,
            $COLUMN_ADD_ON_TAGS TEXT,
            $COLUMN_DIFFICULTY TEXT,
            $COLUMN_XP_REWARD INTEGER,
            $COLUMN_STAT_REWARD INTEGER,
            $COLUMN_ICON INTEGER
        );
    """

        // SQL: Quote Table
        const val CREATE_TABLE_QUOTE = """
        CREATE TABLE $TABLE_QUOTE (
            $COLUMN_QUOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_QUOTE_TEXT TEXT,
            $COLUMN_QUOTE_AUTHOR TEXT,
            $COLUMN_IS_SAVED INTEGER
        );
    """

        // SQL: User Table
        const val CREATE_TABLE_USER = """
        CREATE TABLE $TABLE_USER (
            $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_LEVEL INTEGER,
            $COLUMN_EXP INTEGER,
            $COLUMN_LEVEL_CAP INTEGER,
            $COLUMN_STR INTEGER,
            $COLUMN_END INTEGER,
            $COLUMN_VIT INTEGER
        );
    """

        // SQL: UserQuestActivity Table
        const val CREATE_TABLE_UQA = """
        CREATE TABLE $TABLE_UQA (
            $COLUMN_UQA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_QUEST_STATUS TEXT,
            $COLUMN_USER_LOGS TEXT,
            $COLUMN_DATE_COMPLETED TEXT,
            $COLUMN_UQA_QUEST_ID INTEGER,
            $COLUMN_UQA_QUOTE_ID INTEGER,
            $COLUMN_UQA_USER_ID INTEGER,
            FOREIGN KEY ($COLUMN_UQA_QUEST_ID) REFERENCES $TABLE_QUEST($COLUMN_QUEST_ID),
            FOREIGN KEY ($COLUMN_UQA_QUOTE_ID) REFERENCES $TABLE_QUOTE($COLUMN_QUOTE_ID),
            FOREIGN KEY ($COLUMN_UQA_USER_ID) REFERENCES $TABLE_USER($COLUMN_USER_ID)
        );
    """

        // SQL: Drop all tables
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
            Quest("3 x 15 Push-ups", "Do 3 sets of 15 push-ups to strengthen your chest and triceps.", "Strength", "Chest, Triceps", "Medium", 50, 1),
            Quest("3 x 10 Pull-ups", "Do 3 sets of 10 pull-ups for upper body power.", "Strength", "Back, Biceps", "Hard", 80, 1),
            Quest("60s Plank", "Hold a plank for 60 seconds for core strength.", "Vitality", "Core, Balance", "Medium", 60, 2),
            Quest("10km Jog", "Jog 10 kilometers to build endurance and stamina.", "Endurance", "Cardio, Stamina", "Hard", 120, 3),
            Quest("3 x 30 Jumping Jacks", "Do 3 sets of 30 jumping jacks to warm up and activate full body.", "Endurance", "Warm-up, Cardio", "Easy", 30, 1)
        )

        for (q in quests) {
            val values = ContentValues().apply {
                put(DbReferences.COLUMN_TITLE, q.title)
                put(DbReferences.COLUMN_DESCRIPTION, q.description)
                put(DbReferences.COLUMN_TAG, q.tag)
                put(DbReferences.COLUMN_ADD_ON_TAGS, q.addOnTags)
                put(DbReferences.COLUMN_DIFFICULTY, q.difficulty)
                put(DbReferences.COLUMN_XP_REWARD, q.xpReward)
                put(DbReferences.COLUMN_STAT_REWARD, q.statReward)
            }
            db.insert(DbReferences.TABLE_QUEST, null, values)
        }
    }

    // CRUD FOR QUEST
    /* 🟢 Read: Returns all quests in a list */
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
                    title = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_TITLE)),
                    description = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_DESCRIPTION)),
                    tag = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_TAG)),
                    addOnTags = c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_ADD_ON_TAGS)),
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

    /* 🟢 Insert: Adds a new quest to the DB and returns success */
    @Synchronized
    fun insertQuest(q: Quest): Long {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_TITLE, q.title)
            put(DbReferences.COLUMN_DESCRIPTION, q.description)
            put(DbReferences.COLUMN_TAG, q.tag)
            put(DbReferences.COLUMN_ADD_ON_TAGS, q.addOnTags)
            put(DbReferences.COLUMN_DIFFICULTY, q.difficulty)
            put(DbReferences.COLUMN_XP_REWARD, q.xpReward)
            put(DbReferences.COLUMN_STAT_REWARD, q.statReward)
        }

        val id = database.insert(DbReferences.TABLE_QUEST, null, values)

        database.close()
        return id
    }

    /* 🟡 Update: Modify an existing quest */
    fun updateQuest(q: Quest) {
        val database = this.writableDatabase

        val values = ContentValues().apply {
            put(DbReferences.COLUMN_TITLE, q.title)
            put(DbReferences.COLUMN_DESCRIPTION, q.description)
            put(DbReferences.COLUMN_TAG, q.tag)
            put(DbReferences.COLUMN_ADD_ON_TAGS, q.addOnTags)
            put(DbReferences.COLUMN_DIFFICULTY, q.difficulty)
            put(DbReferences.COLUMN_XP_REWARD, q.xpReward)
            put(DbReferences.COLUMN_STAT_REWARD, q.statReward)
        }

        // This builds the WHERE clause for the update:
        val selection = DbReferences.COLUMN_QUEST_ID + " = ?" //where clause with a placeholder "?"
        val selectionArgs = arrayOf(q.id.toString()) //fills the placeholder

        // Executes the update. Updates the row that matches the id.
        // Closes the database after the operation.
        database.update(DbReferences.TABLE_QUEST, values, selection, selectionArgs)

        database.close()
    }

    /* 🔴 Delete: Remove a quest by its ID */
    fun deleteQuest(id: Int) {
        val database = this.writableDatabase

        // WHERE clause
        val selection = DbReferences.COLUMN_QUEST_ID + " = ?"
        val selectionArgs = arrayOf(id.toString())

        // Executes the delete
        database.delete(DbReferences.TABLE_QUEST, selection, selectionArgs)

        database.close()
    }


    // TODO: The other 3 tables' CRUD operations

    /* QUOTE TABLE CRUD */
    fun insertQuote(quote: Quote): Long {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_QUOTE_TEXT, quote.quoteText)
            put(DbReferences.COLUMN_QUOTE_AUTHOR, quote.quoteAuthor)
            put(DbReferences.COLUMN_IS_SAVED, if (quote.isSaved) 1 else 0)
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

    /* USER TABLE CRUD */
    fun insertUser(user: User): Long {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_LEVEL, user.level)
            put(DbReferences.COLUMN_EXP, user.currentExp)
            put(DbReferences.COLUMN_LEVEL_CAP, user.levelCap)
            put(DbReferences.COLUMN_STR, user.strengthPts)
            put(DbReferences.COLUMN_END, user.endurancePts)
            put(DbReferences.COLUMN_VIT, user.vitalityPts)
        }
        val id = database.insert(DbReferences.TABLE_USER, null, values)
        database.close()
        return id
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
                level = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LEVEL)),
                currentExp = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_EXP)),
                levelCap = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_LEVEL_CAP)),
                strengthPts = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_STR)),
                endurancePts = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_END)),
                vitalityPts = cursor.getInt(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_VIT))
            )
        }
        cursor.close()
        database.close()
        return user
    }

    fun updateUser(user: User) {
        val database = this.writableDatabase
        val values = ContentValues().apply {
            put(DbReferences.COLUMN_LEVEL, user.level)
            put(DbReferences.COLUMN_EXP, user.currentExp)
            put(DbReferences.COLUMN_LEVEL_CAP, user.levelCap)
            put(DbReferences.COLUMN_STR, user.strengthPts)
            put(DbReferences.COLUMN_END, user.endurancePts)
            put(DbReferences.COLUMN_VIT, user.vitalityPts)
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
            put(DbReferences.COLUMN_DATE_COMPLETED, uqa.dateCompleted)
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
                    dateCompleted = cursor.getString(cursor.getColumnIndexOrThrow(DbReferences.COLUMN_DATE_COMPLETED)),
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
            put(DbReferences.COLUMN_DATE_COMPLETED, uqa.dateCompleted)
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

}

