package com.example.solofit.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.solofit.model.Quest

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
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "quest_table"

        const val _ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TAG = "tag"
        const val COLUMN_ADD_ON_TAGS = "add_on_tags"
        const val COLUMN_DIFFICULTY = "difficulty"
        const val COLUMN_XP_REWARD = "xp_reward"
        const val COLUMN_STAT_REWARD = "stat_reward"
        const val COLUMN_ICON = "icon"

        // Creates the Quest Table
        const val CREATE_TABLE_STATEMENT = """
            CREATE TABLE $TABLE_NAME (
                $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
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

        // TODO: Create Quote table

        // TODO: Create User table

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    /* Step 3: Create the table when DB is first initialized */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
        // Log.d("Created table", "Table created successfully")
        insertInitialQuests(db)
    }

    /* Step 4: Drop and recreate the table when DB version changes */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DbReferences.DROP_TABLE_STATEMENT)
        onCreate(db)
    }

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
            db.insert(DbReferences.TABLE_NAME, null, values)
        }
    }

    /* 🟢 Read: Returns all quests in a list */
    fun getAllQuests(): ArrayList<Quest> {
        val database = this.readableDatabase
        val c = database.query(
            DbReferences.TABLE_NAME,
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
                    id = c.getInt(c.getColumnIndexOrThrow(DbReferences._ID)),
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

        val id = database.insert(DbReferences.TABLE_NAME, null, values)

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
        val selection = DbReferences._ID + " = ?" //where clause with a placeholder "?"
        val selectionArgs = arrayOf(q.id.toString()) //fills the placeholder

        // Executes the update. Updates the row that matches the id.
        // Closes the database after the operation.
        database.update(DbReferences.TABLE_NAME, values, selection, selectionArgs)

        database.close()
    }

    /* 🔴 Delete: Remove a quest by its ID */
    fun deleteQuest(id: Int) {
        val database = this.writableDatabase

        // WHERE clause
        val selection = DbReferences._ID + " = ?"
        val selectionArgs = arrayOf(id.toString())

        // Executes the delete
        database.delete(DbReferences.TABLE_NAME, selection, selectionArgs)

        database.close()
    }
}

