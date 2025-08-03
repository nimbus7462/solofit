package com.example.solofit.model

import android.os.Parcel
import android.os.Parcelable

class UserQuestActivity : Parcelable {
    var userQuestActID: Int = 0
    var questStatus: String
    var userLogs: String
    var dateCreated: String
    var questID: Int
    var quoteID: Int
    var userID: Int


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


    private constructor(parcel: Parcel) {
        userQuestActID = parcel.readInt()
        questStatus = parcel.readString() ?: ""
        userLogs = parcel.readString() ?: ""
        dateCreated = parcel.readString() ?: ""
        questID = parcel.readInt()
        quoteID = parcel.readInt()
        userID = parcel.readInt()
    }

    constructor() {
        this.userQuestActID = 0
        this.questStatus = ""
        this.userLogs = ""
        this.dateCreated = ""
        this.questID = 0
        this.quoteID = 0
        this.userID = 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(userQuestActID)
        dest.writeString(questStatus)
        dest.writeString(userLogs)
        dest.writeString(dateCreated)
        dest.writeInt(questID)
        dest.writeInt(quoteID)
        dest.writeInt(userID)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UserQuestActivity> {
        override fun createFromParcel(parcel: Parcel): UserQuestActivity {
            return UserQuestActivity(parcel)
        }

        override fun newArray(size: Int): Array<UserQuestActivity?> {
            return arrayOfNulls(size)
        }
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
