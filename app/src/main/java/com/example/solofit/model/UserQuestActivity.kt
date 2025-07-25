package com.example.solofit.model

class UserQuestActivity {
    var userQuestActID: Int = 0           // Primary Key
    var questStatus: String
    var userLogs: String
    var dateCreated: String               // Format: "YYYY-MM-DD"
    var questID: Int                      // Foreign Key to Quest
    var quoteID: Int                      // Foreign Key to Quote
    var userID: Int                       // Foreign Key to User

    // Constructor without ID (before DB assigns ID)
    constructor(
        questStatus: String,
        userLogs: String,
        dateCreated: String,
        questID: Int,
        quoteID: Int,
        userID: Int
    ) {
        this.questStatus = questStatus
        this.userLogs = userLogs
        this.dateCreated = dateCreated
        this.questID = questID
        this.quoteID = quoteID
        this.userID = userID
    }

    // Constructor with ID (when loading from DB)
    constructor(
        userQuestActID: Int,
        questStatus: String,
        userLogs: String,
        dateCreated: String,
        questID: Int,
        quoteID: Int,
        userID: Int
    ) {
        this.userQuestActID = userQuestActID
        this.questStatus = questStatus
        this.userLogs = userLogs
        this.dateCreated = dateCreated
        this.questID = questID
        this.quoteID = quoteID
        this.userID = userID
    }

    override fun toString(): String {
        return "UserQuestActivity{" +
                "userQuestActID=$userQuestActID" +
                ", questStatus='$questStatus'" +
                ", userLogs='$userLogs'" +
                ", dateCreated='$dateCreated'" +
                ", questID=$questID" +
                ", quoteID=$quoteID" +
                ", userID=$userID" +
                '}'
    }
}
