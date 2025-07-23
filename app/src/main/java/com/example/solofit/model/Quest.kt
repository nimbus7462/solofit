package com.example.solofit.model

/*
# 1. Defining the Data Model: Quest

- The Quest model represents an exercise or task in the fitness app.
- It contains details like:
  - *ID, Title, Description*
  - *Tag, AddOnTags* – used for filtering or categorization
  - *Difficulty* – e.g., Easy, Medium, Hard
  - *XP Reward* and *Stat Reward* – used for gamification
- We'll provide:
  - A full constructor with ID (from database)
  - A constructor without ID (for insertion before DB auto-generates it)
 */

class Quest {
    var id: Int = 0
        set
    var title: String
        set
    var description: String
        set
    var tag: String
        set
    var addOnTags: String
        set
    var difficulty: String
        set
    var xpReward: Int
        set
    var statReward: Int
        set

    // Constructor without ID – used before inserting into DB
    constructor(title: String, description: String, tag: String, addOnTags: String, difficulty: String, xpReward: Int, statReward: Int) {
        this.title = title
        this.description = description
        this.tag = tag
        this.addOnTags = addOnTags
        this.difficulty = difficulty
        this.xpReward = xpReward
        this.statReward = statReward
    }

    // Constructor with ID – used when loading from DB
    constructor(id: Int, title: String, description: String, tag: String, addOnTags: String, difficulty: String, xpReward: Int, statReward: Int) {
        this.id = id
        this.title = title
        this.description = description
        this.tag = tag
        this.addOnTags = addOnTags
        this.difficulty = difficulty
        this.xpReward = xpReward
        this.statReward = statReward
    }

    // Optional: toString() for logging/debug
    override fun toString(): String {
        return "Quest{" +
                "id=$id" +
                ", title='$title'" +
                ", description='$description'" +
                ", tag='$tag'" +
                ", addOnTags='$addOnTags'" +
                ", difficulty='$difficulty'" +
                ", xpReward=$xpReward" +
                ", statReward=$statReward" +
                '}'
    }
}
