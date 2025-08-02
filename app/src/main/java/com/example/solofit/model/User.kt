package com.example.solofit.model

class User {
    var userID: Int = 0
    var username: String
    var pfpUri: String?
    var selectedTitle: String  // Replaces userTitle
    var level: Int
    var currentExp: Float
    var expCap: Int
    var strengthPts: Int
    var endurancePts: Int
    var vitalityPts: Int
    var streakCount: Int
    var longestStreak: Int
    var lastStreakDate: String?
    var unlockedTitles: String = ""
    // Constructor without ID
    constructor(
        username: String,
        pfpUri: String?,
        selectedTitle: String,
        level: Int,
        currentExp: Float,
        expCap: Int,
        strengthPts: Int,
        endurancePts: Int,
        vitalityPts: Int,
        streakCount: Int,
        longestStreak: Int,
        lastStreakDate: String?,
        unlockedTitles: String,
    ) {
        this.username = username
        this.pfpUri = pfpUri
        this.selectedTitle = selectedTitle
        this.level = level
        this.currentExp = currentExp
        this.expCap = expCap
        this.strengthPts = strengthPts
        this.endurancePts = endurancePts
        this.vitalityPts = vitalityPts
        this.streakCount = streakCount
        this.longestStreak = longestStreak
        this.lastStreakDate = lastStreakDate
        this.unlockedTitles = unlockedTitles
    }

    // Constructor with ID
    constructor(
        userID: Int,
        username: String,
        pfpUri: String?,
        selectedTitle: String,
        level: Int,
        currentExp: Float,
        expCap: Int,
        strengthPts: Int,
        endurancePts: Int,
        vitalityPts: Int,
        streakCount: Int,
        longestStreak: Int,
        lastStreakDate: String?,
        unlockedTitles: String,
    ) {
        this.userID = userID
        this.username = username
        this.pfpUri = pfpUri
        this.selectedTitle = selectedTitle
        this.level = level
        this.currentExp = currentExp
        this.expCap = expCap
        this.strengthPts = strengthPts
        this.endurancePts = endurancePts
        this.vitalityPts = vitalityPts
        this.streakCount = streakCount
        this.longestStreak = longestStreak
        this.lastStreakDate = lastStreakDate
        this.unlockedTitles = unlockedTitles
    }

    override fun toString(): String {
        return "User{" +
                "userID=$userID" +
                ", username='$username'" +
                ", pfp=$pfpUri" +
                ", selectedTitle='$selectedTitle'" +
                ", level=$level" +
                ", currentExp=$currentExp" +
                ", levelCap=$expCap" +
                ", strengthPts=$strengthPts" +
                ", endurancePts=$endurancePts" +
                ", vitalityPts=$vitalityPts" +
                ", streakCount=$streakCount" +
                ", longestStreak=$longestStreak" +
                ", lastStreakDate=$lastStreakDate" +
                ", unlockedTitles=$unlockedTitles" +
                '}'
    }
}
