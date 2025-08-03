package com.example.solofit.model

class Quest {
    var id: Int = 0
        set
    var questName: String
        set
    var description: String
        set
    var questType: String
        set
    var extraTags: String
        set
    var difficulty: String
        set
    var xpReward: Int
        set
    var statReward: Int
        set

    constructor(questName: String, description: String, questType: String, extraTags: String, difficulty: String, xpReward: Int, statReward: Int) {
        this.questName = questName
        this.description = description
        this.questType = questType
        this.extraTags = extraTags
        this.difficulty = difficulty
        this.xpReward = xpReward
        this.statReward = statReward
    }

    constructor(id: Int, questName: String, description: String, questType: String, extraTags: String, difficulty: String, xpReward: Int, statReward: Int) {
        this.id = id
        this.questName = questName
        this.description = description
        this.questType = questType
        this.extraTags = extraTags
        this.difficulty = difficulty
        this.xpReward = xpReward
        this.statReward = statReward
    }


    override fun toString(): String {
        return "Quest{" +
                "id=$id" +
                ", questName='$questName'" +
                ", description='$description'" +
                ", questType='$questType'" +
                ", extraTags='$extraTags'" +
                ", difficulty='$difficulty'" +
                ", xpReward=$xpReward" +
                ", statReward=$statReward" +
                '}'
    }
}

